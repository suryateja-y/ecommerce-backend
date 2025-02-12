package com.academy.projects.ecommerce.notificationservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRegister {
    @CustomUpdate
    private String templateName;
    @CustomUpdate
    private String subject;
    @CustomUpdate
    private List<String> cc = new LinkedList<>();
    @CustomUpdate
    private List<String> bcc = new LinkedList<>();
}
