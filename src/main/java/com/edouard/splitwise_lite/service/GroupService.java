package com.edouard.splitwise_lite.service;

import com.edouard.splitwise_lite.entity.Expense;
import com.edouard.splitwise_lite.entity.Group;
import com.edouard.splitwise_lite.entity.User;
import com.edouard.splitwise_lite.repository.ExpenseRepository;
import com.edouard.splitwise_lite.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public Group createGroup(String name, String creatorEmail) {
        User creator = userService.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Group group = Group.builder()
                .name(name)
                .members(List.of(creator))
                .build();

        return groupRepository.save(group);
    }

    public Group addMember(Long groupId, String memberEmail) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));

        User user = userService.findByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        group.getMembers().add(user);
        return groupRepository.save(group);
    }

    public List<Group> getGroupsForUser(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return groupRepository.findByMembersId(user.getId());
    }

    public Map<String, BigDecimal> calculateBalances(Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));

        Map<String, BigDecimal> balances = new HashMap<>();

        for (User member : group.getMembers()) {
            balances.put(member.getEmail(), BigDecimal.ZERO);
        }

        int memberCount = group.getMembers().size();

        for (Expense expense : expenses) {
            BigDecimal share = expense.getAmount()
                    .divide(BigDecimal.valueOf(memberCount), 2, RoundingMode.HALF_UP);

            String payer = expense.getPaidBy().getEmail();
            balances.put(payer, balances.get(payer).add(expense.getAmount()).subtract(share));

            for (User member : group.getMembers()) {
                if (!member.getEmail().equals(payer)) {
                    balances.put(member.getEmail(), balances.get(member.getEmail()).subtract(share));
                }
            }
        }

        return balances;
    }
}