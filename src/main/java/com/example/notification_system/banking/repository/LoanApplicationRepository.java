package com.example.notification_system.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notification_system.banking.model.LoanApplication;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    List<LoanApplication> findByUserId(Long userId);
}
