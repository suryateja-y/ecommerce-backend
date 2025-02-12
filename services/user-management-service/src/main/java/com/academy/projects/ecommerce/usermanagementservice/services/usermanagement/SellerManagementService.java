package com.academy.projects.ecommerce.usermanagementservice.services.usermanagement;

import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.SignUpRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.configurations.Patcher;
import com.academy.projects.ecommerce.usermanagementservice.dtos.SellerRegistrationDto;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.IdNotProvidedException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.InvalidateStateRequest;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.UserAlreadyExistsException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.UserNotFoundException;
import com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user.ISellerUpdateManager;
import com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user.IUserUpdateManager;
import com.academy.projects.ecommerce.usermanagementservice.models.*;
import com.academy.projects.ecommerce.usermanagementservice.repositories.SellerRepository;
import com.academy.projects.ecommerce.usermanagementservice.services.approvalmanagement.SellerApprovalManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerManagementService implements ISellerManagementService {
    private final SellerRepository sellerRepository;
    private final IUserManagementService userManagementService;
    private final SellerApprovalManager sellerApprovalManager;
    private final ISellerUpdateManager sellerUpdateManager;
    private final IUserUpdateManager userUpdateManager;

    private final Patcher patcher;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(SellerManagementService.class);

    @Autowired
    public SellerManagementService(final SellerRepository sellerRepository, IUserManagementService userManagementService, SellerApprovalManager sellerApprovalManager, ISellerUpdateManager sellerUpdateManager, IUserUpdateManager userUpdateManager, Patcher patcher, PasswordEncoder passwordEncoder) {
        this.sellerRepository = sellerRepository;
        this.userManagementService = userManagementService;
        this.sellerApprovalManager = sellerApprovalManager;
        this.sellerUpdateManager = sellerUpdateManager;
        this.userUpdateManager = userUpdateManager;
        this.patcher = patcher;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Seller register(SellerRegistrationDto sellerDto) {
        if(userManagementService.userExists(sellerDto.getEmail(), UserType.SELLER)) throw new UserAlreadyExistsException(sellerDto.getEmail(), UserType.SELLER);

        // Registering Seller into the Authentication Service
        String userId = userManagementService.registerInAuthenticationService(SignUpRequestDto.builder()
                .email(sellerDto.getEmail())
                .password(passwordEncoder.encode(sellerDto.getPassword()))
                .userType(UserType.SELLER)
                .build());

        User user = new User();
        user.setEmail(sellerDto.getEmail());
        user.setFullName(sellerDto.getFullName());
        user.setUserType(UserType.SELLER);
        user.setUserState(UserState.PENDING_FOR_APPROVAL);
        user.setPhoneNumber(sellerDto.getPhoneNumber());
        user.setId(userId);
        User newUser = userManagementService.save(user);

        Seller seller = new Seller();
        seller.setBrandName(sellerDto.getBrandName());
        seller.setCompanyName(sellerDto.getCompanyName());
        seller.setUser(newUser);
        seller.setId(userId);

        // Sending seller for the approval
        String approvalRequestId = this.sellerApprovalManager.register(seller);

        logger.info("Seller Approval Request Id: {}", approvalRequestId);

        seller.setApprovalId(approvalRequestId);
        seller = sellerRepository.save(seller);

        // Send notification with approval request details
        sellerUpdateManager.sendRegistration(seller);

        return seller;
    }

    @Override
    public Seller update(Seller seller) {
        if(seller.getId() == null) throw new IdNotProvidedException();
        Seller savedSeller = sellerRepository.findById(seller.getId()).orElseThrow(() -> new UserNotFoundException(seller.getId()));
        User savedUser = savedSeller.getUser();
        patcher.entity(savedUser, savedSeller.getUser(), User.class);
        patcher.entity(savedSeller, seller, Seller.class);
        savedUser.setId(savedSeller.getId());
        savedSeller.setUser(savedUser);

        // Sending Seller for the approval
        String approvalRequestId = this.sellerApprovalManager.register(savedSeller);
        logger.info("Seller Update Approval Request Id: {}", approvalRequestId);

        // Save into the Database
        savedUser = userManagementService.save(savedUser);
        savedSeller.setUser(savedUser);
        savedSeller.setApprovalId(approvalRequestId);
        savedSeller = sellerRepository.save(savedSeller);

        // Send notification with approval request details
        sellerUpdateManager.sendUpdate(savedSeller);
        return savedSeller;
    }

    @Override
    public Seller getSeller(String userId) {
        return sellerRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<Seller> getSellers(int page, int pageSize, UserState userState) {
        if(userState != null) return this.getSellersByUserState(page, pageSize, userState);
        Pageable pageable = PageRequest.of(page, pageSize);
        return sellerRepository.findAll(pageable).getContent();
    }

    private List<Seller> getSellersByUserState(int page, int pageSize, UserState userState) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Seller> sellerPage = sellerRepository.findAllByUser_UserState(userState, pageable);
        return sellerPage.getContent();
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public Seller updateState(String userId, UserState userState, String updaterId) {
        if(!(userState.equals(UserState.IN_ACTIVE) || userState.equals(UserState.REJECTED))) throw new InvalidateStateRequest(userState);
        if(!sellerRepository.existsById(userId)) throw new UserNotFoundException(userId);
        userManagementService.updateState(userId, userState);
        User user = userManagementService.get(userId);
        Seller savedSeller = sellerRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        savedSeller.setUser(user);
        Seller seller = sellerRepository.save(savedSeller);

        // Send update to Kafka
        userUpdateManager.sendUserUpdate(seller.getUser(), seller.getApprovalId(), updaterId);
        return seller;
    }

    @Override
    public Seller addAddress(String sellerId, Address address) {
        Seller savedSeller = sellerRepository.findById(sellerId).orElseThrow(() -> new UserNotFoundException(sellerId));
        savedSeller.setAddress(address);
        User user = savedSeller.getUser();
        user.setUserState(UserState.PENDING_FOR_APPROVAL);
        user = userManagementService.save(user);
        savedSeller.setUser(user);

        // Sending Seller for the approval
        String approvalRequestId = this.sellerApprovalManager.register(savedSeller);
        logger.info("Seller Address Approval Request Id: {}", approvalRequestId);

        savedSeller.setApprovalId(approvalRequestId);
        Seller seller = sellerRepository.save(savedSeller);

        // Send a notification to the Seller
        sellerUpdateManager.sendAddressUpdate(seller);
        return seller;
    }

    @Override
    public Seller updateAddress(String sellerId, Address address) {
        if(address.getId() == null) throw new IdNotProvidedException();
        return this.addAddress(address.getId(), address);
    }

    @Override
    public Address getAddress(String sellerId) {
        Seller savedSeller = sellerRepository.findById(sellerId).orElseThrow(() -> new UserNotFoundException(sellerId));
        return savedSeller.getAddress();
    }
}
