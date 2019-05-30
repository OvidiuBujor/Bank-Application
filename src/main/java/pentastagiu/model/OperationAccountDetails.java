package pentastagiu.model;

import pentastagiu.convertor.OperationType;

import java.math.BigDecimal;

public class OperationAccountDetails {
    private Long id;

    private BigDecimal amount;

    private OperationType operation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }
}
