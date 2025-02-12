package com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user;

import com.academy.projects.ecommerce.usermanagementservice.dtos.ActionType;
import com.academy.projects.ecommerce.usermanagementservice.kafka.dtos.UserUpdateDto;
import com.academy.projects.ecommerce.usermanagementservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateManager implements IUserUpdateManager {
    @Value("${application.kafka.topics.user-update-topic}")
    private String userUpdateTopic;

    private final KafkaTemplate<String, UserUpdateDto> userUpdateProducer;

    @Autowired
    public UserUpdateManager(KafkaTemplate<String, UserUpdateDto> userUpdateProducer) {
        this.userUpdateProducer = userUpdateProducer;
    }


    @Override
    public void sendUserUpdate(User user, String approvalId, String updaterId) {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .action(ActionType.UPDATE)
                .user(user)
                .approvalId(approvalId)
                .updaterId(updaterId)
                .build();
        userUpdateProducer.send(userUpdateTopic, userUpdateDto);
    }
}
