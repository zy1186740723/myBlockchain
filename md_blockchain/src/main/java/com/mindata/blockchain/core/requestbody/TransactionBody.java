package com.mindata.blockchain.core.requestbody;

/**
 * @Author: zhangyan
 * @Date: 2019/4/13 10:27
 * @Version 1.0
 */
public class TransactionBody {
    /**
     * 交易的具体操作方式
     */
    private byte operation;
    /**
     *具体内容,新的请求的内容
     */
    private String json;

    public String getOldJson() {
        return oldJson;
    }

    public void setOldJson(String oldJson) {
        this.oldJson = oldJson;
    }

    /**
     * 删除和更新操作需要有之前交易的内容
     */
    private String oldJson;

    private String transactionId;


    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 公钥
     */
    private String publicKey;
    @Override
    public String toString() {
        return "InstructionBody{" +
                "operation=" + operation +
                ", json='" + json + '\'' +
                ", instructionId='" + transactionId + '\'' +
                '}';
    }

    public byte getOperation() {
        return operation;
    }

    public void setOperation(byte operation) {
        this.operation = operation;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }



    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
