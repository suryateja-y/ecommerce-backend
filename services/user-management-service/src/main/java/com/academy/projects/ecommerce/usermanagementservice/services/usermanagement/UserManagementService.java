package com.academy.projects.ecommerce.usermanagementservice.services.usermanagement;

import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.SignUpRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.SignUpResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.clients.services.AuthenticationServiceClient;
import com.academy.projects.ecommerce.usermanagementservice.clients.configuration.FeignResponseDecoder;
import com.academy.projects.ecommerce.usermanagementservice.dtos.FeignErrorResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.UserValidityCheckResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.InternalErrorResponseException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.UserNotFoundException;
import com.academy.projects.ecommerce.usermanagementservice.models.User;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import com.academy.projects.ecommerce.usermanagementservice.models.UserType;
import com.academy.projects.ecommerce.usermanagementservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagementService implements IUserManagementService {

    private final UserRepository userRepository;
    private final AuthenticationServiceClient authenticationServiceClient;
    private final FeignResponseDecoder feignResponseDecoder;
    private final Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    public UserManagementService(UserRepository userRepository, AuthenticationServiceClient authenticationServiceClient, FeignResponseDecoder feignResponseDecoder) {
        this.userRepository = userRepository;
        this.authenticationServiceClient = authenticationServiceClient;
        this.feignResponseDecoder = feignResponseDecoder;
    }

    @Override
    public boolean userExists(String email, UserType userType) {
        return userRepository.existsByEmailAndUserType(email, userType);
    }

    @Override
    public String registerInAuthenticationService(SignUpRequestDto requestDto) {
        try {
            // Register the user into the Authentication Service
            SignUpResponseDto responseDto = authenticationServiceClient.register(requestDto);
            logger.info(responseDto.getMessage());
            return responseDto.getUserId();
        } catch (Exception e) {
            logger.error(e.getMessage());
            FeignErrorResponseDto<SignUpResponseDto> responseDto = feignResponseDecoder.decode(e, SignUpResponseDto.class);
            throw new InternalErrorResponseException(responseDto.getHttpStatus(), responseDto.getResponse().getMessage());
        }
    }

    @Override
    public void updateState(String userId, UserState userState) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        // Updating state in Authentication Service
        authenticationServiceClient.updateState(userId, userState);

        user.setUserState(userState);
        userRepository.save(user);
    }

    @Override
    public User get(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserValidityCheckResponseDto isValid(String userId, UserType type) {
        UserValidityCheckResponseDto responseDto = new UserValidityCheckResponseDto();
        responseDto.setValid(false);
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            if(!user.getUserState().equals(UserState.APPROVED))
                responseDto.setMessage("User is not approved yet!!!");
            else if(!user.getUserType().equals(type))
                responseDto.setMessage("User is not a " + type + "!!!");
            else {
                responseDto.setValid(true);
                responseDto.setMessage("User is valid!!!");
            }
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
        }
        return responseDto;
    }

    @Override
    public List<User> getUsers(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<User> oPage = userRepository.findAll(pageable);
        return oPage.getContent();
    }

}
