package com.example.notification_system.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notification_system.banking.model.BankUser;

public interface BankUserRepository extends JpaRepository<BankUser, Long> {

    boolean existsByEmail(String email);
}
