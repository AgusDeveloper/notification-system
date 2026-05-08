package com.example.notification_system.banking.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.notification_system.banking.dto.CreateLoanRequest;
import com.example.notification_system.banking.dto.LoanResponse;
import com.example.notification_system.banking.dto.UpdateLoanRequest;
import com.example.notification_system.banking.model.BankUser;
import com.example.notification_system.banking.model.LoanApplication;
import com.example.notification_system.banking.repository.BankUserRepository;
import com.example.notification_system.banking.repository.LoanApplicationRepository;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository loanRepository;
    private final BankUserRepository userRepository;
    private final BankingNotificationPublisher notificationPublisher;

    public LoanApplicationService(
            LoanApplicationRepository loanRepository,
            BankUserRepository userRepository,
            BankingNotificationPublisher notificationPublisher
    ) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.notificationPublisher = notificationPublisher;
    }

    @Transactional
    public LoanResponse create(CreateLoanRequest request) {
        BankUser user = findUser(request.userId());
        LoanApplication loan = loanRepository.save(new LoanApplication(
                user,
                request.amount(),
                request.termMonths(),
                request.purpose()
        ));
        notificationPublisher.publish("LOAN_REQUESTED", user.getEmail(), "Loan requested for amount: " + loan.getAmount());
        return LoanResponse.from(loan);
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findAll() {
        return loanRepository.findAll().stream()
                .map(LoanResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findByUserId(Long userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(LoanResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public LoanResponse findById(Long id) {
        return LoanResponse.from(findLoan(id));
    }

    @Transactional
    public LoanResponse update(Long id, UpdateLoanRequest request) {
        LoanApplication loan = findLoan(id);
        loan.update(request.amount(), request.termMonths(), request.purpose(), request.status());
        notificationPublisher.publish(
                "LOAN_UPDATED",
                loan.getUser().getEmail(),
                "Loan " + loan.getId() + " status changed to " + loan.getStatus()
        );
        return LoanResponse.from(loan);
    }

    @Transactional
    public void delete(Long id) {
        LoanApplication loan = findLoan(id);
        String email = loan.getUser().getEmail();
        loanRepository.delete(loan);
        notificationPublisher.publish("LOAN_DELETED", email, "Loan application deleted: " + id);
    }

    private BankUser findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank user not found"));
    }

    private LoanApplication findLoan(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan application not found"));
    }
}
