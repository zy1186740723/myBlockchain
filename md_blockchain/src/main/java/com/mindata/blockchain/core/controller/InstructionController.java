package com.mindata.blockchain.core.controller;

import com.mindata.blockchain.block.Transaction;
import com.mindata.blockchain.core.bean.BaseData;
import com.mindata.blockchain.core.bean.ResultGenerator;
import com.mindata.blockchain.core.requestbody.InstructionBody;
import com.mindata.blockchain.core.requestbody.TransactionBody;
import com.mindata.blockchain.core.service.InstructionService;
import com.mindata.blockchain.core.service.TransactionService;
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
    private InstructionService instructionService;
    @Resource
    private TransactionService transactionService;

    private Logger logger = LoggerFactory.getLogger(InstructionController.class);

    /**
     * 构建一条指令，传入各必要参数
     * @param instructionBody instructionBody
     * @return
     * 用私钥签名后的指令
     */
    @PostMapping
    public BaseData build(@RequestBody InstructionBody instructionBody) throws Exception {
        if (!instructionService.checkKeyPair(instructionBody)) {
             return ResultGenerator.genFailResult("公私钥不是一对");
        }
        if (!instructionService.checkContent(instructionBody)) {
            return ResultGenerator.genFailResult("Delete和Update操作需要有id和json内容");
        }
        return ResultGenerator.genSuccessResult(instructionService.build(instructionBody));
    }

    public static int count=0;
    //public static HashMap<Integer,Transaction> hashMap=new HashMap<>();
    //public static ArrayList<Transaction> arrayList;
    @PostMapping("/transaction")
    public BaseData buildTransacton
            (@RequestBody TransactionBody transactionBody) throws Exception{
        //HashMap<Integer,>
        if (!transactionService.checkKeyPair(transactionBody)){
            return ResultGenerator.genFailResult("公钥私钥不是一对");
        }
        if (!transactionService.checkContent(transactionBody)){
            return ResultGenerator.genFailResult("内容有问题");
        }

        //构造交易
        Transaction transaction=transactionService.build(transactionBody);
        //返回成功的信息
        BaseData baseData=ResultGenerator.genSuccessResult(transaction);
//        HashMap<Integer,Transaction> hashMap=new HashMap<>();

        ArrayList<Transaction> arrayList=transactionService.countTools(transaction);
        count++;
        System.out.println(arrayList.size());
        arrayList.size();
//        hashMap.size();
//        logger.info(hashMap.toString());
        return baseData;

    }
}
