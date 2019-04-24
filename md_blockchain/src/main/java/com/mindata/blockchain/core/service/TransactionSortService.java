package com.mindata.blockchain.core.service;

import com.mindata.blockchain.block.Transaction;
import org.springframework.stereotype.Service;

import java.util.Comparator;

/**
 * @Author: zhangyan
 * @Date: 2019/4/24 9:58
 * @Version 1.0
 */
@Service
public class TransactionSortService implements Comparator<Transaction> {
    @Override
    public int compare(Transaction tx1, Transaction tx2) {

        if (tx1.getTimeStamp()>tx2.getTimeStamp())
            return  1;
        else
            return -1;


    }
}
