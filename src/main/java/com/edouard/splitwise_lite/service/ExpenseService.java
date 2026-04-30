package com.edouard.splitwise_lite.service;

import com.edouard.splitwise_lite.entity.Expense;
import com.edouard.splitwise_lite.entity.Group;
import com.edouard.splitwise_lite.entity.User;
import com.edouard.splitwise_lite.repository.ExpenseRepository;
import com.edouard.splitwise_lite.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final UserService userService;

    public Expense addExpense(Long groupId, String payerEmail, String description, BigDecimal amount) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));

        User payer = userService.findByEmail(payerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        boolean isMember = group.getMembers().stream()
                .anyMatch(m -> m.getEmail().equals(payerEmail));

        if (!isMember) {
            throw new RuntimeException("L'utilisateur n'est pas membre du groupe");
        }

        Expense expense = Expense.builder()
                .description(description)
                .amount(amount)
                .paidBy(payer)
                .group(group)
                .build();

        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByGroup(Long groupId) {
        return expenseRepository.findByGroupId(groupId);
    }
}