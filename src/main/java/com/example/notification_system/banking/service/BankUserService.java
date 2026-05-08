package com.example.notification_system.banking.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.notification_system.banking.dto.CreateUserRequest;
import com.example.notification_system.banking.dto.UpdateUserRequest;
import com.example.notification_system.banking.dto.UserResponse;
import com.example.notification_system.banking.model.BankUser;
import com.example.notification_system.banking.repository.BankUserRepository;
import com.example.notification_system.banking.repository.LoanApplicationRepository;

@Service
public class BankUserService {

    private final BankUserRepository userRepository;
    private final LoanApplicationRepository loanRepository;
    private final BankingNotificationPublisher notificationPublisher;

    public BankUserService(
            BankUserRepository userRepository,
            LoanApplicationRepository loanRepository,
            BankingNotificationPublisher notificationPublisher
    ) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.notificationPublisher = notificationPublisher;
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        ensureEmailIsAvailable(request.email());

        BankUser user = userRepository.save(new BankUser(
                request.fullName(),
                request.email(),
                request.phoneNumber()
        ));
        notificationPublisher.publish("USER_CREATED", user.getEmail(), "Bank user created: " + user.getFullName());
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return UserResponse.from(findUser(id));
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        BankUser user = findUser(id);
        if (!user.getEmail().equals(request.email())) {
            ensureEmailIsAvailable(request.email());
        }

        user.update(request.fullName(), request.email(), request.phoneNumber(), request.status());
        notificationPublisher.publish("USER_UPDATED", user.getEmail(), "Bank user updated: " + user.getFullName());
        return UserResponse.from(user);
    }

    @Transactional
    public void delete(Long id) {
        BankUser user = findUser(id);
        loanRepository.deleteAll(loanRepository.findByUserId(id));
        userRepository.delete(user);
        notificationPublisher.publish("USER_DELETED", user.getEmail(), "Bank user deleted: " + user.getFullName());
    }

    private BankUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank user not found"));
    }

    private void ensureEmailIsAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
    }
}
