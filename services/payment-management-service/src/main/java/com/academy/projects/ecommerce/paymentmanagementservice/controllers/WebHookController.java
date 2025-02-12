package com.academy.projects.ecommerce.paymentmanagementservice.controllers;

import com.academy.projects.ecommerce.paymentmanagementservice.exceptions.PaymentNotFoundException;
import com.academy.projects.ecommerce.paymentmanagementservice.services.IWebHookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/${application.version}/webhooks")
public class WebHookController {
    private final IWebHookService webHookService;

    private final Logger logger = LoggerFactory.getLogger(WebHookController.class);

    @Autowired
    public WebHookController(final IWebHookService webHookService) {
        this.webHookService = webHookService;
    }

    @PostMapping("")
    public void handleWebHooks(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature) {
        webHookService.handleEvent(payload, signature);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(PaymentNotFoundException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
