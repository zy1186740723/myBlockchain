package com.mindata.blockchain.block;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 区块body，里面存放交易的数组
 * @author wuweifeng wrote on 2018/2/28.
 */
public class BlockBody {
    private List<Transaction> transactions;

    @Override
    public String toString() {
        return "BlockBody{" +
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
