package com.academy.projects.ecommerce.usermanagementservice.services.approvalmanagement;

import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.ApprovalResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.ResponseStatus;
import com.academy.projects.ecommerce.usermanagementservice.clients.services.ApprovalManagementServiceClient;
import com.academy.projects.ecommerce.usermanagementservice.dtos.ActionType;
import com.academy.projects.ecommerce.usermanagementservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.usermanagementservice.dtos.ApprovalStatus;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.SellerApprovalRegistrationException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.UserNotFoundException;
import com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user.SellerUpdateManager;
import com.academy.projects.ecommerce.usermanagementservice.models.Seller;
import com.academy.projects.ecommerce.usermanagementservice.models.User;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import com.academy.projects.ecommerce.usermanagementservice.repositories.SellerRepository;
import com.academy.projects.ecommerce.usermanagementservice.repositories.UserRepository;
import com.academy.projects.ecommerce.usermanagementservice.starters.GlobalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerApprovalManager implements IUserApprovalManager {

    @Value("${application.kafka.topics.user-approval-topic}")
    private String sellerApprovalTopic;

    private final ApprovalManagementServiceClient approvalManagementServiceClient;
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final SellerUpdateManager sellerUpdateManager;

    @Autowired
    public SellerApprovalManager(ApprovalManagementServiceClient approvalManagementServiceClient, SellerRepository sellerRepository, UserRepository userRepository, SellerUpdateManager sellerUpdateManager) {
        this.approvalManagementServiceClient = approvalManagementServiceClient;
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
        this.sellerUpdateManager = sellerUpdateManager;
    }

    public String register(Seller seller) {
        try {
            ApprovalRequest approvalRequest = getApprovalRequest(seller);
            ApprovalResponseDto responseDto = approvalManagementServiceClient.registerUserForApproval(approvalRequest);
            if(responseDto.getResponseStatus() == ResponseStatus.SUCCESS)
                return responseDto.getApprovalId();
            else throw new SellerApprovalRegistrationException(seller.getId());
        } catch(Exception e) {
            throw new SellerApprovalRegistrationException(seller.getId());
        }
    }

    private ApprovalRequest getApprovalRequest(Seller seller) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setRequester(seller.getId());
        approvalRequest.setTopic(sellerApprovalTopic);
        approvalRequest.setTitle("Seller Approval");
        approvalRequest.setData(seller);
        approvalRequest.setApprovers(List.of(GlobalData.SELLER_MANAGER_ID, GlobalData.ADMIN_ID));
        approvalRequest.setActionType(ActionType.CREATE);
        return approvalRequest;
    }

    @Override
    public void updateStatus(ApprovalRequest approvalRequest) {
        Seller seller = sellerRepository.findById(approvalRequest.getRequester()).orElseThrow(() -> new UserNotFoundException(approvalRequest.getRequester()));
        User user = seller.getUser();
        user.setUserState(from(approvalRequest.getStatus()));
        user = userRepository.save(user);
        seller.setUser(user);
        sellerRepository.save(seller);

        // Sending seller update to the consumers
        this.sellerUpdateManager.sendStatusUpdate(seller);
    }

    private UserState from(ApprovalStatus approvalStatus) {
        if(approvalStatus == ApprovalStatus.APPROVED) return UserState.APPROVED;
        if(approvalStatus == ApprovalStatus.REJECTED) return UserState.REJECTED;
        return UserState.PENDING_FOR_APPROVAL;
    }
}
