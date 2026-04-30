package com.edouard.splitwise_lite.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class AddExpenseRequest {
    private String description;
    private BigDecimal amount;
}