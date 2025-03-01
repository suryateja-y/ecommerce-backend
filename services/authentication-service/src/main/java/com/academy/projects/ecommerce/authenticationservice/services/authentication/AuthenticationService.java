package com.academy.projects.ecommerce.authenticationservice.services.authentication;

import com.academy.projects.ecommerce.authenticationservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.LoginRequestDto;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.SignUpRequestDto;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.SignUpResponseDto;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.UserPermissionsDto;
import com.academy.projects.ecommerce.authenticationservice.models.*;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authentication.UserAlreadyExistsException;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authentication.UserNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.RoleNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.repositories.authentication.UserRepository;
import com.academy.projects.ecommerce.authenticationservice.repositories.authorization.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final IdGenerator idGenerator;
    private final ITokenService tokenService;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


    @Autowired
    public AuthenticationService(final AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, IdGenerator idGenerator, ITokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.idGenerator = idGenerator;
        this.tokenService = tokenService;
    }

    @Override
    public String login(LoginRequestDto requestDto) {
        User user = this.userRepository.findByEmailAndUserType(requestDto.getEmail(), requestDto.getUserType()).orElseThrow(() -> new UserNotFoundException(requestDto.getEmail(), requestDto.getUserType()));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), requestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenService.generateToken(authentication.getName());
        logger.trace("User '{}' logged in successfully!!!", requestDto.getEmail());
        return token;
    }

    @Override
    public SignUpResponseDto signup(SignUpRequestDto requestDto) {
        SignUpResponseDto responseDto = new SignUpResponseDto();
        Optional<User> existingUser = this.userRepository.findByEmailAndUserType(requestDto.getEmail(), requestDto.getUserType());
        if (existingUser.isPresent())
            throw new UserAlreadyExistsException(requestDto.getEmail(), requestDto.getUserType());
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setUserType(requestDto.getUserType());
        user.setPassword(requestDto.getPassword());
        user.setId(idGenerator.getId(User.SEQUENCE_NAME));

        // Seller and employee needs approval process
        if (requestDto.getUserType() == UserType.CUSTOMER)
            user.setUserState(UserState.APPROVED);

        // Adding default roles
        user.setRoles(this.getDefaultRoles(requestDto.getUserType()));
        user = userRepository.save(user);

        responseDto.setMessage("User created successfully!!!");
        responseDto.setUserId(user.getId());
        responseDto.setCreatedAt(user.getCreatedAt());

        logger.info("User: '{}' has been created successfully!!!", requestDto.getEmail());
        return responseDto;
    }

    private Set<Role> getDefaultRoles(UserType userType) {

        // Creating USER role
        Role userRole = roleRepository.findByRoleName("USER").orElseThrow(() -> new RoleNotFoundException("USER"));

        // Creating SELLER role
        Role sellerRole = roleRepository.findByRoleName("SELLER").orElseThrow(() -> new RoleNotFoundException("SELLER"));

        // Creating CUSTOMER role
        Role customerRole = roleRepository.findByRoleName("CUSTOMER").orElseThrow(() -> new RoleNotFoundException("CUSTOMER"));

        // Creating EMPLOYEE role
        Role employeeRole = roleRepository.findByRoleName("EMPLOYEE").orElseThrow(() -> new RoleNotFoundException("EMPLOYEE"));

        return switch (userType) {
            case SELLER -> Set.of(userRole, sellerRole);
            case CUSTOMER -> Set.of(userRole, customerRole);
            case EMPLOYEE -> Set.of(userRole, employeeRole);
        };
    }

    @Override
    public UserPermissionsDto validateAndGetRoles(String token) {
        String message = "Token Validation Successful!!!", userId = "";
        List<String> rolesAndPermissions = new ArrayList<>();
        UserState userState = UserState.PENDING_FOR_APPROVAL;
        UserPermissionsDto dto = new UserPermissionsDto();
        try {
            if (token == null) message = "Token should not be empty!!!";
            userId = tokenService.validate(token);
            User user = userRepository.findById(userId).orElse(null);
            if(user == null) message = "User not found!!!";
            else if (!user.getUserState().equals(UserState.APPROVED)) message = "Invalid User!!!";
            else {
                rolesAndPermissions = getRolesAndPermissions(user);
                userState = user.getUserState();
            }
        } catch (Exception e) {
            message = e.getMessage();
        } finally {
            dto.setMessage(message);
            dto.setRolesAndPermissions(rolesAndPermissions);
            dto.setUserId(userId);
            dto.setUserState(userState);
        }
        return dto;
    }

    private List<String> getRolesAndPermissions(User user) {
        List<String> rolesAndPermissions = new ArrayList<>();
        for(Role role : user.getRoles()) {
            rolesAndPermissions.add("ROLE_" + role.getRoleName());
            for(Permission permission : role.getPermissions()) {
                rolesAndPermissions.add(permission.getPermissionName());
            }
        }
        return rolesAndPermissions;
    }
}
