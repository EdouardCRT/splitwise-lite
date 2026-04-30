package com.edouard.splitwise_lite.controller;

import com.edouard.splitwise_lite.dto.AddExpenseRequest;
import com.edouard.splitwise_lite.dto.AddMemberRequest;
import com.edouard.splitwise_lite.dto.CreateGroupRequest;
import com.edouard.splitwise_lite.entity.Expense;
import com.edouard.splitwise_lite.entity.Group;
import com.edouard.splitwise_lite.service.ExpenseService;
import com.edouard.splitwise_lite.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest request,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        Group group = groupService.createGroup(request.getName(), userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<Group> addMember(@PathVariable Long groupId,
                                           @RequestBody AddMemberRequest request) {
        Group group = groupService.addMember(groupId, request.getEmail());
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<List<Group>> getMyGroups(@AuthenticationPrincipal UserDetails userDetails) {
        List<Group> groups = groupService.getGroupsForUser(userDetails.getUsername());
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/{groupId}/expenses")
    public ResponseEntity<Expense> addExpense(@PathVariable Long groupId,
                                              @RequestBody AddExpenseRequest request,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        Expense expense = expenseService.addExpense(
                groupId,
                userDetails.getUsername(),
                request.getDescription(),
                request.getAmount()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(expense);
    }

    @GetMapping("/{groupId}/expenses")
    public ResponseEntity<List<Expense>> getExpenses(@PathVariable Long groupId) {
        List<Expense> expenses = expenseService.getExpensesByGroup(groupId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{groupId}/balances")
    public ResponseEntity<Map<String, BigDecimal>> getBalances(@PathVariable Long groupId) {
        Map<String, BigDecimal> balances = groupService.calculateBalances(groupId);
        return ResponseEntity.ok(balances);
    }
}