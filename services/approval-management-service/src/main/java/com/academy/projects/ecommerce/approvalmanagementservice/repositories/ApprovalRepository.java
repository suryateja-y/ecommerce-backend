package com.academy.projects.ecommerce.approvalmanagementservice.repositories;

import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends MongoRepository<ApprovalRequest, String> {
    List<ApprovalRequest> findAllByTitleAndRequester(String title, String requester);
    Page<ApprovalRequest> findAllByRequesterOrApproversIsContainingAndStatus(String requester, String approverId, ApprovalStatus approvalStatus, Pageable pageable);
    Page<ApprovalRequest> findAllByRequesterOrApproversIsContaining(String requester, String approverId, Pageable pageable);
    Page<ApprovalRequest> findAllByStatus(ApprovalStatus approvalStatus, Pageable pageable);
}
