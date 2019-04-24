package com.mindata.blockchain.block;

import java.util.List;

/**
 * @Author: zhangyan
 * @Date: 2019/4/15 15:08
 * @Version 1.0
 */
public class BlockBodyTX {
    private List<Transaction> transactions;

    @Override
    public String toString() {
        return "BlockBodyTX{" +
                "transactions=" + transactions +
                '}';
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
