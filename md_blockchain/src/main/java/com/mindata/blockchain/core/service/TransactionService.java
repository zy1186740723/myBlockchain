package com.mindata.blockchain.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.mindata.blockchain.block.Block;
import com.mindata.blockchain.block.Instruction;
import com.mindata.blockchain.block.Operation;
import com.mindata.blockchain.block.Transaction;
import com.mindata.blockchain.common.CommonUtil;
import com.mindata.blockchain.common.Sha256;
import com.mindata.blockchain.common.TrustSDK;
import com.mindata.blockchain.common.exception.TrustSDKException;
import com.mindata.blockchain.core.bean.BaseData;
import com.mindata.blockchain.core.bean.ResultCode;
import com.mindata.blockchain.core.bean.ResultGenerator;
import com.mindata.blockchain.core.controller.InstructionController;
import com.mindata.blockchain.core.manager.DbBlockManager;
import com.mindata.blockchain.core.requestbody.BlockRequestBody;
import com.mindata.blockchain.core.requestbody.TransactionBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: zhangyan
 * @Date: 2019/4/13 11:41
 * @Version 1.0
 */

@Service
public class TransactionService {
    @Resource
    private BlockService blockService;
    @Resource
    private addNewBlockService addNewBlockService;
    @Resource
    private TestUtilsService testUtilsService;
    @Resource
    private DbBlockManager dbBlockManager;
    @Resource
    private TransactionSortService transactionSortService;

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


    private Logger logger = LoggerFactory.getLogger(TransactionService.class);
    //private static ConcurrentLinkedQueue<String> concurrentLinkedQueue=new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<Integer> concurrentLinkedQueue=new ConcurrentLinkedQueue<>();
    public static ArrayList<Transaction> arrayList=new ArrayList<>();
    public synchronized ArrayList<Transaction> countTools (Transaction transaction) throws IOException {
        //TODO 更换同步synchronized的方式，提高并发效率
        /**
         * 需要对交易进行排序，在这里进行操作
         * 这是唯一的区别，没有主节点
         * 通过一个随机路由的算法，随机主节点的方式
         * 在每个主节点内进行交易的排序
         * 保证区块的顺序是确定的
         * 类似于超级长辈中的solo模式：单节点排序
         */
        arrayList.add(transaction);
        sort(arrayList,transactionSortService);
        logger.info(arrayList.toString());

        if (arrayList.size() >= 10) {
            //logger.info(dbBlockManager.getLastBlock().toString());
            int lastBlockNumber=dbBlockManager.getLastBlockNumber();
                logger.info("执行区块生成请求");
                BlockRequestBody res = addNewBlockService.TxBlockBuild(arrayList);

                logger.info(res.toString());
                Long startTime = System.nanoTime();
                Block block = blockService.addBlock(res);
                logger.info(block.toString());
                BaseData baseData1 = ResultGenerator.genSuccessResult(block);
                //当区块在数据库中完成入库操作以后，再进行之后的操作
                while (dbBlockManager.getLastBlockNumber()==lastBlockNumber){

                }



                Long endTime = System.nanoTime();
                double time = (endTime - startTime) / 1000000000.0;
                //区块编号
                int number = block.getBlockHeader().getNumber();
                concurrentLinkedQueue.add(number);
                logger.info(concurrentLinkedQueue.toString());
                //时间的字符串输出
                String timeRes = "第" + number + "个区块的生成时间：" + Double.toString(time) + ",";
                //将区块生成时间写text文档中
                testUtilsService.recordAddBlockTime(timeRes);
                logger.info(Double.toString(time));
                //timeResL.clear();
                arrayList.clear();
                return arrayList;
            }


            logger.info(arrayList.toString());

            return arrayList;

    }

    public boolean checkSign(Transaction transaction) throws TrustSDKException {
        String buildStr = getSignString(transaction);
        return TrustSDK.verifyString(transaction.getPublicKey(), buildStr, transaction.getSign());
    }

    public boolean checkHash(Transaction transaction) {
        String buildStr = getSignString(transaction);
        return Sha256.sha256(buildStr).equals(transaction.getHash());
    }

    public void sort(ArrayList arrayList,TransactionSortService transactionSortService){
        Collections.sort(arrayList,transactionSortService);

    }





}
