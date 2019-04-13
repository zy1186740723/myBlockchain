package com.mindata.blockchain.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.mindata.blockchain.block.Operation;
import com.mindata.blockchain.block.Transaction;
import com.mindata.blockchain.common.CommonUtil;
import com.mindata.blockchain.common.Sha256;
import com.mindata.blockchain.common.TrustSDK;
import com.mindata.blockchain.common.exception.TrustSDKException;
import com.mindata.blockchain.core.requestbody.TransactionBody;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @Author: zhangyan
 * @Date: 2019/4/13 11:41
 * @Version 1.0
 */

@Service
public class TransactionService {

    /**
     *
     * @param transactionBody
     * @return
     * @throws Exception
     */
    public boolean checkKeyPair(TransactionBody transactionBody) throws Exception{
        return TrustSDK.checkPairKey(transactionBody.getPrivateKey(),transactionBody.getPublicKey());
    }


    /**
     * 检查交易内容的合法性
     * @param transactionBody
     * @return
     * @throws Exception
     */
    public boolean checkContent(TransactionBody transactionBody)throws Exception{
        byte operation=transactionBody.getOperation();
        if (operation != Operation.ADD && operation != Operation.DELETE && operation != Operation.UPDATE) {
            return false;
        }

        return Operation.UPDATE !=operation&&Operation.DELETE!=operation || transactionBody.getOldJson()!=null
                && transactionBody.getJson()!=null && transactionBody.getTransactionId()!=null;

    }

    public Transaction build(TransactionBody transactionBody) throws  Exception{
        Transaction transaction=new Transaction();
        BeanUtil.copyProperties(transactionBody,transaction);
        //如果是增操作，为交易生成序列号
        if (transaction.getOperation()==Operation.ADD){
            transaction.setTransactionId(CommonUtil.generateUuid());
        }
        //生成时间戳
        transaction.setTimeStamp(CommonUtil.getNow());
        //需要进行签名的内容
        String buildStr=getSignString(transaction);
        //设置签名
        transaction.setSign(TrustSDK.signString(transactionBody.getPrivateKey(),buildStr));
        transaction.setHash(Sha256.sha256(buildStr));
        return transaction;



    }

    public String getSignString(Transaction transaction){
        return transaction.getOperation()+transaction.getTransactionId()+transaction.getTimeStamp()
                +(transaction.getJson()==null?"":transaction.getJson());

    }

    public static ArrayList<Transaction> arrayList=new ArrayList<>();
    public ArrayList<Transaction> countTools(Transaction transaction){

        arrayList.add(transaction);
        return arrayList;

    }





}
