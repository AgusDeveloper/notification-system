package com.example.notification_system.banking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification_system.banking.dto.CreateLoanRequest;
import com.example.notification_system.banking.dto.LoanResponse;
import com.example.notification_system.banking.dto.UpdateLoanRequest;
import com.example.notification_system.banking.service.LoanApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bank/loans")
public class LoanApplicationController {

    private final LoanApplicationService loanService;

    public LoanApplicationController(LoanApplicationService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse create(@Valid @RequestBody CreateLoanRequest request) {
        return loanService.create(request);
    }

    @GetMapping
    public List<LoanResponse> findAll(@RequestParam(required = false) Long userId) {
        if (userId != null) {
            return loanService.findByUserId(userId);
        }
        return loanService.findAll();
    }

    @GetMapping("/{id}")
    public LoanResponse findById(@PathVariable Long id) {
        return loanService.findById(id);
    }

    @PutMapping("/{id}")
    public LoanResponse update(@PathVariable Long id, @Valid @RequestBody UpdateLoanRequest request) {
        return loanService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        loanService.delete(id);
    }
}
