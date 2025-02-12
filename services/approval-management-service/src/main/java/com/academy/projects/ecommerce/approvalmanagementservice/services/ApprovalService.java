package com.academy.projects.ecommerce.approvalmanagementservice.services;

import com.academy.projects.ecommerce.approvalmanagementservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.approvalmanagementservice.dtos.UpdateStatusRequestDto;
import com.academy.projects.ecommerce.approvalmanagementservice.exceptions.*;
import com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos.UpdateType;
import com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.publishers.IApprovalUpdatePublisher;
import com.academy.projects.ecommerce.approvalmanagementservice.models.Action;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.approvalmanagementservice.repositories.ApprovalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ApprovalService implements IApprovalService {

    private final ApprovalRepository approvalRepository;
    private final IdGenerator idGenerator;
    private final IApprovalUpdatePublisher approvalUpdatePublisher;

    private final Logger logger = LoggerFactory.getLogger(ApprovalService.class);

    @Autowired
    public ApprovalService(ApprovalRepository approvalRepository, IdGenerator idGenerator, IApprovalUpdatePublisher approvalUpdatePublisher) {
        this.approvalRepository = approvalRepository;
        this.idGenerator = idGenerator;
        this.approvalUpdatePublisher = approvalUpdatePublisher;
    }

    @Override
    public ApprovalRequest register(ApprovalRequest approvalRequest) {
        ApprovalRequest savedApprovalRequest = getAnyOpenRequest(approvalRequest);
        if(savedApprovalRequest != null) {
            approvalRequest.setId(savedApprovalRequest.getId());
            return update(approvalRequest, approvalRequest.getRequester());
        }
        this.checkAnyOpenRequests(approvalRequest);
        approvalRequest.setStatus(ApprovalStatus.PENDING_FOR_APPROVAL);

        Action action = Action.builder()
                .actedBy("System")
                .approvalStatus(ApprovalStatus.PENDING_FOR_APPROVAL)
                .comment("Registered the Approval Request")
                .actedOn(new Date())
                .build();

        approvalRequest.setActions(List.of(action));
        approvalRequest.setCreatedAt(action.getActedOn());
        approvalRequest.setPreviousAssignedTo("");
        approvalRequest.setAssignedTo(approvalRequest.getApprovers().get(0));

        String approvalId = idGenerator.getId("Approval");
        approvalRequest.setId(approvalId);

        ApprovalRequest registeredRequest = this.approvalRepository.save(approvalRequest);

        logger.info("New Registration: {}", registeredRequest.getId());

        // Send to Kafka
        approvalUpdatePublisher.notify(registeredRequest, UpdateType.CREATE);

        return registeredRequest;
    }

    @Override
    public ApprovalRequest update(ApprovalRequest approvalRequest, String requester) {
        if(approvalRequest.getId() == null) throw new IdNotProvidedException();
        ApprovalRequest registeredRequest = this.approvalRepository.findById(approvalRequest.getId()).orElseThrow(() -> new ApprovalRequestNotFoundException(approvalRequest.getId()));
        if(!registeredRequest.getRequester().equals(requester)) throw new NoAccessToChangeApprovalRequestException(requester);

        List<String> updates = new ArrayList<>();

        if(approvalRequest.getRequester() != null) {
            updates.add("Requester changed -> from: '" + registeredRequest.getRequester() + "' -> to: '" + approvalRequest.getRequester() + "'");
            registeredRequest.setRequester(approvalRequest.getRequester());
        }

        if(approvalRequest.getTitle() != null) {
            updates.add("Title changed -> from: '" + registeredRequest.getTitle() + "' -> to: '" + approvalRequest.getTitle() + "'");
            registeredRequest.setTitle(approvalRequest.getTitle());
        }

        if(approvalRequest.getData() != null) {
            updates.add("Data changed -> from: '" + registeredRequest.getData() + "' -> to: '" + approvalRequest.getData() + "'");
            registeredRequest.setData(approvalRequest.getData());
        }

        if(approvalRequest.getActionType() != null) {
            updates.add("Action Type changed -> from: '" + registeredRequest.getActionType() + "' -> to: '" + approvalRequest.getActionType() + "'");
            registeredRequest.setActionType(approvalRequest.getActionType());
        }

        Action action = Action.builder()
                .comment(String.join("\n", updates))
                .actedOn(new Date())
                .actedBy(requester)
                .approvalStatus(ApprovalStatus.PENDING_FOR_APPROVAL)
                .build();
        registeredRequest.setStatus(ApprovalStatus.PENDING_FOR_APPROVAL);
        registeredRequest.setPreviousAssignedTo(registeredRequest.getAssignedTo());
        registeredRequest.setAssignedTo(registeredRequest.getApprovers().get(0));

        registeredRequest.getActions().add(action);
        ApprovalRequest updatedRequest = this.approvalRepository.save(registeredRequest);

        logger.info("Approval Request: '{}' has been updated successfully!!!", updatedRequest.getId());

        // Send to Kafka
        approvalUpdatePublisher.notify(updatedRequest, UpdateType.UPDATE);

        return updatedRequest;
    }

    @Override
    public ApprovalRequest updateStatus(UpdateStatusRequestDto requestDto, String id, String requester) {
        ApprovalRequest registeredRequest = this.approvalRepository.findById(id).orElseThrow(() -> new ApprovalRequestNotFoundException(id));
        registeredRequest.setPreviousAssignedTo(registeredRequest.getAssignedTo());

        if(requester.equalsIgnoreCase(registeredRequest.getRequester())) {
            this.updateFromRequester(requestDto, registeredRequest, requester);
        } else if(requester.equalsIgnoreCase(registeredRequest.getAssignedTo())) {
            this.updateFromApprover(requestDto, registeredRequest, requester);
        } else throw new NoAccessToChangeApprovalRequestException(requester);
        registeredRequest = this.approvalRepository.save(registeredRequest);

        approvalUpdatePublisher.notify(registeredRequest, UpdateType.STATUS_UPDATE);

        logger.info("Approval Request: '{}' status has been updated to '{}' successfully!!!", registeredRequest.getId(), registeredRequest.getStatus());
        return registeredRequest;
    }

    @Override
    // This is the direct update by User Managers from User Management Service
    public ApprovalRequest updateStatus(String approvalId, ApprovalStatus approvalStatus, String requester) {
        if(!(approvalStatus.equals(ApprovalStatus.CANCELLED) || approvalStatus.equals(ApprovalStatus.REJECTED))) throw new NoAccessToChangeApprovalRequestException(approvalStatus);
        ApprovalRequest registeredRequest = this.approvalRepository.findById(approvalId).orElseThrow(() -> new ApprovalRequestNotFoundException(approvalId));
        registeredRequest.setStatus(approvalStatus);

        Action action = Action.builder()
                .actedOn(new Date())
                .comment("Direct status update by " + requester)
                .approvalStatus(approvalStatus)
                .actedBy(requester)
                .build();
        registeredRequest.getActions().add(action);

        registeredRequest = this.approvalRepository.save(registeredRequest);

        logger.info("Approval Request: '{}' Direct Status Update status has been updated to '{}' successfully!!!", registeredRequest.getId(), registeredRequest.getStatus());

        approvalUpdatePublisher.notify(registeredRequest, UpdateType.STATUS_UPDATE);

        return registeredRequest;
    }

    @Override
    public ApprovalRequest getApprovalRequest(String approvalId, String requester) {
        ApprovalRequest registeredRequest = this.approvalRepository.findById(approvalId).orElseThrow(() -> new ApprovalRequestNotFoundException(approvalId));
        if(requester.equalsIgnoreCase(registeredRequest.getRequester()) || registeredRequest.getApprovers().contains(requester)) return registeredRequest;
        else throw new NoAccessToChangeApprovalRequestException(requester);
    }

    private boolean isRequestCompleted(ApprovalRequest approvalRequest) {
        return !switch (approvalRequest.getStatus()) {
            case PENDING_FOR_APPROVAL, NEED_FURTHER_APPROVAL, NEED_MORE_INFORMATION -> true;
            default -> false;
        };
    }

    @Override
    public ApprovalRequest getApprovalRequest(String approvalId) {
        return this.approvalRepository.findById(approvalId).orElseThrow(() -> new ApprovalRequestNotFoundException(approvalId));
    }

    @Override
    public List<ApprovalRequest> getApprovalRequestsOfUser(String requester, int page, int pageSize, ApprovalStatus approvalStatus) {
        if(approvalStatus != null) return this.getApprovalRequestsOfUserAndApprovalStatus(requester, page, pageSize, approvalStatus);
        Pageable pageable = PageRequest.of(page, pageSize);
        return approvalRepository.findAllByRequesterOrApproversIsContaining(requester, requester, pageable).getContent();
    }

    @Override
    public List<ApprovalRequest> getApprovalRequests(int page, int pageSize, ApprovalStatus approvalStatus) {
        if(approvalStatus != null) return this.getApprovalRequestsByApprovalStatus(page, pageSize, approvalStatus);
        Pageable pageable = PageRequest.of(page, pageSize);
        return approvalRepository.findAll(pageable).getContent();
    }

    private List<ApprovalRequest> getApprovalRequestsByApprovalStatus(int page, int pageSize, ApprovalStatus approvalStatus) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return approvalRepository.findAllByStatus(approvalStatus, pageable).getContent();
    }

    private List<ApprovalRequest> getApprovalRequestsOfUserAndApprovalStatus(String requester, int page, int pageSize, ApprovalStatus approvalStatus) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return approvalRepository.findAllByRequesterOrApproversIsContainingAndStatus(requester, requester, approvalStatus, pageable).getContent();
    }


    private void checkAnyOpenRequests(ApprovalRequest approvalRequest) {
        List<ApprovalRequest> approvalRequests = this.approvalRepository.findAllByTitleAndRequester(approvalRequest.getTitle(), approvalRequest.getRequester());
        ApprovalRequest openRequest = approvalRequests.stream().filter(request -> (request.getStatus() != ApprovalStatus.APPROVED) && (request.getStatus() != ApprovalStatus.REJECTED)).findFirst().orElse(null);
        if(openRequest != null) throw new OpenRequestExistsException(openRequest.getId());
    }

    private ApprovalRequest getAnyOpenRequest(ApprovalRequest approvalRequest) {
        List<ApprovalRequest> approvalRequests = this.approvalRepository.findAllByTitleAndRequester(approvalRequest.getTitle(), approvalRequest.getRequester());
        return approvalRequests.stream().filter(request -> (request.getStatus() != ApprovalStatus.APPROVED) && (request.getStatus() != ApprovalStatus.REJECTED)).findFirst().orElse(null);
    }

    private void updateFromRequester(UpdateStatusRequestDto requestDto, ApprovalRequest approvalRequest, String requester) {
        if(isRequestCompleted(approvalRequest)) throw new ApprovalProcessAlreadyCompletedException(approvalRequest.getId());
        ApprovalStatus approvalStatus = requestDto.getApprovalStatus().equals(ApprovalStatus.CANCELLED) ? ApprovalStatus.CANCELLED : ApprovalStatus.PENDING_FOR_APPROVAL;
        String assignee = requestDto.getApprovalStatus().equals(ApprovalStatus.CANCELLED) ? requester : approvalRequest.getApprovers().get(0);
        Action action = Action.builder()
                .approvalStatus(approvalStatus)
                .actedBy(requester)
                .actedOn(new Date())
                .comment(requestDto.getComment())
                .build();
        approvalRequest.setStatus(approvalStatus);
        approvalRequest.setAssignedTo(assignee);
        approvalRequest.getActions().add(action);
    }

    private void updateFromApprover(UpdateStatusRequestDto requestDto, ApprovalRequest approvalRequest, String approver) {
        if(isRequestCompleted(approvalRequest)) throw new ApprovalProcessAlreadyCompletedException(approvalRequest.getId());
        switch (requestDto.getApprovalStatus()) {
            case PENDING_FOR_APPROVAL:
            case NEED_MORE_INFORMATION: this.pendingByApprover(requestDto, approvalRequest, approver); break;
            case APPROVED: this.approvedByApprover(requestDto, approvalRequest, approver); break;
            case REJECTED: this.rejectedByApprover(requestDto, approvalRequest, approver); break;
            default: throw new InvalidApprovalStatusException(requestDto.getApprovalStatus());
        }

    }

    private void pendingByApprover(UpdateStatusRequestDto requestDto, ApprovalRequest approvalRequest, String approver) {
        Action action = Action.builder()
                .approvalStatus(ApprovalStatus.NEED_MORE_INFORMATION)
                .actedBy(approver)
                .actedOn(new Date())
                .comment(requestDto.getComment())
                .build();
        approvalRequest.setStatus(ApprovalStatus.NEED_MORE_INFORMATION);
        approvalRequest.getActions().add(action);
        approvalRequest.setAssignedTo(approvalRequest.getRequester());
    }

    private void rejectedByApprover(UpdateStatusRequestDto requestDto, ApprovalRequest approvalRequest, String approver) {
        Action action = Action.builder()
                .approvalStatus(ApprovalStatus.REJECTED)
                .actedBy(approver)
                .actedOn(new Date())
                .comment(requestDto.getComment())
                .build();
        approvalRequest.setStatus(ApprovalStatus.REJECTED);
        approvalRequest.getActions().add(action);
        approvalRequest.setAssignedTo(approvalRequest.getRequester());
    }

    private void approvedByApprover(UpdateStatusRequestDto requestDto, ApprovalRequest approvalRequest, String approver) {
        int approverIndex = approvalRequest.getApprovers().indexOf(approver);
        ApprovalStatus status = ApprovalStatus.NEED_FURTHER_APPROVAL;
        // Remove this line in future
        if(approverIndex == -1) approverIndex = approvalRequest.getApprovers().size() - 1;
        if(approverIndex == approvalRequest.getApprovers().size() - 1) status = ApprovalStatus.APPROVED;

        Action action = Action.builder()
                .approvalStatus(status)
                .actedBy(approver)
                .actedOn(new Date())
                .comment(requestDto.getComment())
                .build();
        approvalRequest.setStatus(status);
        approvalRequest.getActions().add(action);
        if(status == ApprovalStatus.NEED_FURTHER_APPROVAL)
            approvalRequest.setAssignedTo(approvalRequest.getApprovers().get(approverIndex + 1));
    }
}
