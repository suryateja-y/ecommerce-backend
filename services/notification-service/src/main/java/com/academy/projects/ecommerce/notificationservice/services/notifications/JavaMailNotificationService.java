package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.exceptions.EmailAddressNotProvidedException;
import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.EmailRegister;
import com.academy.projects.ecommerce.notificationservice.models.Setting;
import com.academy.projects.ecommerce.notificationservice.exceptions.EmailDetailsNotAvailableException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class JavaMailNotificationService implements IEmailNotificationService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final INotificationArchiveService notificationArchiveService;

    private final Logger logger = LoggerFactory.getLogger(JavaMailNotificationService.class);

    public JavaMailNotificationService(JavaMailSender mailSender, TemplateEngine templateEngine, INotificationArchiveService notificationArchiveService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.notificationArchiveService = notificationArchiveService;
    }

    @Override
    public void send(Setting register, Map<String, Object> data, RecipientCommunicationDetails recipientDetails) {
        EmailRegister emailRegister = register.getEmailRegister();
        if(emailRegister == null) throw new EmailDetailsNotAvailableException(register.getRegistryKey());
        if(recipientDetails.getEmail() == null) throw new EmailAddressNotProvidedException(register.getRegistryKey());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setTo(recipientDetails.getEmail());
            String subject = (emailRegister.getSubject() == null) ? "Notification from Academy ECommerce Application" : buildSubject(data, emailRegister.getSubject());
            helper.setSubject(subject);
            Context context = new Context();

            for(Map.Entry<String, Object> entry : data.entrySet())
                context.setVariable(entry.getKey(), entry.getValue());

            String htmlContent = templateEngine.process(emailRegister.getTemplateName() + ".html", context);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            notificationArchiveService.saveEmailNotification(emailRegister, recipientDetails.getEmail(), subject, htmlContent);
        } catch (MessagingException e) {
            logger.error(e.getMessage());
        }
    }

    private String buildSubject(Map<String, Object> data, String subject) {
        // Replace all variables from the subject line. Variables will be in the format "${variableName}"
        for(Map.Entry<String, Object> entry : data.entrySet()) {
            subject = subject.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return subject;
    }
}
