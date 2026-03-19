package com.ssdlc.library.repository;

import com.ssdlc.library.model.BorrowRequest;
import com.ssdlc.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Long> {
    List<BorrowRequest> findByUser(User user);
    List<BorrowRequest> findByStatus(BorrowRequest.Status status);
    boolean existsByUserAndBookIdAndStatus(User user, Long bookId, BorrowRequest.Status status);
}
