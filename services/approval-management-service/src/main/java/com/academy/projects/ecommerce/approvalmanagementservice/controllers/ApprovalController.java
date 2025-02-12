package com.academy.projects.ecommerce.approvalmanagementservice.controllers;

import com.academy.projects.ecommerce.approvalmanagementservice.dtos.ApprovalRequestResponseDto;
import com.academy.projects.ecommerce.approvalmanagementservice.dtos.ApprovalResponseDto;
import com.academy.projects.ecommerce.approvalmanagementservice.dtos.ResponseStatus;
import com.academy.projects.ecommerce.approvalmanagementservice.dtos.UpdateStatusRequestDto;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.approvalmanagementservice.services.IApprovalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/approvals")
public class ApprovalController {
    private final IApprovalService approvalService;

    private final Logger logger = LoggerFactory.getLogger(ApprovalController.class);

    @Autowired
    public ApprovalController(IApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApprovalResponseDto> register(@RequestBody ApprovalRequest approvalRequest) {
        ApprovalResponseDto responseDto = new ApprovalResponseDto();
        try {
            ApprovalRequest updatedRequest = this.approvalService.register(approvalRequest);
            responseDto.setApprovalId(updatedRequest.getId());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
            responseDto.setMessage("Approval successfully registered!!!");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
            responseDto.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('ROLE_USER') and @accessChecker.isOwner(#approvalRequest.requester)")
    public ResponseEntity<ApprovalResponseDto> update(@RequestBody ApprovalRequest approvalRequest, Authentication authentication) {
        ApprovalResponseDto responseDto = new ApprovalResponseDto();
        try {
            ApprovalRequest updatedRequest = this.approvalService.update(approvalRequest, authentication.getName());
            responseDto.setApprovalId(updatedRequest.getId());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
            responseDto.setMessage("Approval successfully registered!!!");
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
            responseDto.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApprovalResponseDto> updateStatus(@RequestBody UpdateStatusRequestDto requestDto, @PathVariable String id, Authentication authentication) {
        ApprovalResponseDto responseDto = new ApprovalResponseDto();
        try {
            ApprovalRequest approvalRequest = this.approvalService.updateStatus(requestDto, id, authentication.getName());
            responseDto.setApprovalId(approvalRequest.getId());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
            responseDto.setMessage("Approval successfully updated!!!");
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
            responseDto.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/{approvalId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApprovalRequestResponseDto> getApprovalRequest(@PathVariable String approvalId, Authentication authentication) {
        ApprovalRequestResponseDto responseDto = new ApprovalRequestResponseDto();
        try {
            ApprovalRequest request = this.approvalService.getApprovalRequest(approvalId, authentication.getName());
            responseDto.setApprovalRequest(request);
            responseDto.setComment("Retrieved successfully!!!");
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseDto.setApprovalRequest(null);
            responseDto.setComment(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ApprovalRequest>> getApprovalRequests(Authentication authentication, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) ApprovalStatus approvalStatus) {
        List<ApprovalRequest> approvalRequests = approvalService.getApprovalRequestsOfUser(authentication.getName(), page, pageSize, approvalStatus);
        return ResponseEntity.status(HttpStatus.OK).body(approvalRequests);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_APPROVAL_MANAGER')")
    public ResponseEntity<List<ApprovalRequest>> getApprovalRequests(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) ApprovalStatus approvalStatus) {
        List<ApprovalRequest> approvalRequests = approvalService.getApprovalRequests(page, pageSize, approvalStatus);
        return ResponseEntity.status(HttpStatus.OK).body(approvalRequests);
    }

    @GetMapping("/admin/{approvalId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_APPROVAL_MANAGER')")
    public ResponseEntity<ApprovalRequest> getApprovalRequests(@PathVariable String approvalId) {
        ApprovalRequest approvalRequest = approvalService.getApprovalRequest(approvalId);
        return ResponseEntity.status(HttpStatus.OK).body(approvalRequest);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
