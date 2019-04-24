package com.mindata.blockchain.core.controller;

import com.mindata.blockchain.block.Block;
import com.mindata.blockchain.block.Transaction;
import com.mindata.blockchain.core.bean.BaseData;
import com.mindata.blockchain.core.bean.ResultGenerator;
import com.mindata.blockchain.core.requestbody.BlockRequestBody;
import com.mindata.blockchain.core.requestbody.BlockRequestBodyTx;
import com.mindata.blockchain.core.requestbody.InstructionBody;
import com.mindata.blockchain.core.requestbody.TransactionBody;
import com.mindata.blockchain.core.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 区块body内单个指令的controller
 * @author wuweifeng wrote on 2018/3/7.
 */
@ApiIgnore
@RestController
@RequestMapping("/instruction")
public class InstructionController {
    @Resource
    private BlockService blockService;
    @Resource
    private InstructionService instructionService;
    @Resource
    private TransactionService transactionService;
    @Resource
    private addNewBlockService addNewBlockService;
    @Resource
    private TestUtilsService testUtilsService;

    private Logger logger = LoggerFactory.getLogger(InstructionController.class);

    /**
     * 构建一条指令，传入各必要参数
     * @param instructionBody instructionBody
     * @return
     * 用私钥签名后的指令
     */
    @PostMapping()
    public BaseData build(@RequestBody InstructionBody instructionBody) throws Exception {

        if (!instructionService.checkKeyPair(instructionBody)) {
             return ResultGenerator.genFailResult("公私钥不是一对");
        }
        if (!instructionService.checkContent(instructionBody)) {
            return ResultGenerator.genFailResult("Delete和Update操作需要有id和json内容");
        }
        return ResultGenerator.genSuccessResult(instructionService.build(instructionBody));
    }


    @PostMapping("/transaction")
    public BaseData buildTransacton
            (@RequestBody TransactionBody transactionBody) throws Exception{
        //每个线程开始的时间
        long ThreadStartTime=System.currentTimeMillis();
        if (!transactionService.checkKeyPair(transactionBody)){
            return ResultGenerator.genFailResult("公钥私钥不是一对");
        }
        if (!transactionService.checkContent(transactionBody)){
            return ResultGenerator.genFailResult("内容有问题");
        }
        //构造交易
        Transaction transaction=transactionService.build(transactionBody);
        logger.info(transaction.toString());
        //返回成功的信息
        BaseData baseData=ResultGenerator.genSuccessResult(transaction);
        //TODO 多线程处理
        //synchronized (this)
        ArrayList<Transaction> arrayList=transactionService.countTools(transaction);
        //每个线程结束的时间
        long ThreadEndTime=System.currentTimeMillis();
        //结果的检测流程
        System.out.println(arrayList.size());
        logger.info(String.valueOf(arrayList.size()));


        return baseData ;

    }
}
