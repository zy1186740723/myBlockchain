package com.mindata.blockchain.block;

/**
 * @Author: zhangyan
 * @Date: 2019/4/13 11:46
 * @Version 1.0
 */
public class TransactionBase {
    /**
     * 指令的操作，增删改（1，-1，2）
     */
    private byte operation;

    /**
            * 最终要执行进入区块的json内容
     */
    private String finalJson;

    private String transactionId;


    @Override
    public String toString() {
        return "TransactionBase{" +
                "operation=" + operation +
                ", finalJson='" + finalJson + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }

    public byte getOperation() {
        return operation;
    }

    public void setOperation(byte operation) {
        this.operation = operation;
    }

    public String getFinalJson() {
        return finalJson;
    }

    public void setFinalJson(String finalJson) {
        this.finalJson = finalJson;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
