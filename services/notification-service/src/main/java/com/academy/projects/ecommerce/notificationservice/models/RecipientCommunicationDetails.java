package com.academy.projects.ecommerce.notificationservice.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipientCommunicationDetails {
    private String email;
    private String phone;
}
