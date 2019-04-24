package com.mindata.blockchain.core.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mindata.blockchain.block.*;
import com.mindata.blockchain.block.merkle.MerkleTree;
import com.mindata.blockchain.common.CommonUtil;
import com.mindata.blockchain.common.Sha256;
import com.mindata.blockchain.common.exception.TrustSDKException;
import com.mindata.blockchain.core.manager.DbBlockManager;
import com.mindata.blockchain.core.manager.PermissionManager;
import com.mindata.blockchain.core.requestbody.BlockRequestBody;
import com.mindata.blockchain.core.requestbody.BlockRequestBodyTx;
import com.mindata.blockchain.socket.body.RpcBlockBody;
import com.mindata.blockchain.socket.client.PacketSender;
import com.mindata.blockchain.socket.packet.BlockPacket;
import com.mindata.blockchain.socket.packet.PacketBuilder;
import com.mindata.blockchain.socket.packet.PacketType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuweifeng wrote on 2018/3/8.
 */
@Service
public class BlockService {
    @Resource
    private InstructionService instructionService;
    @Resource
    private TransactionService transactionService;
    @Value("${version}")
    private int version;
    @Resource
    private PacketSender packetSender;
    @Resource
    private DbBlockManager dbBlockManager;
    @Resource
    private PermissionManager permissionManager;

    /**
     * 校验指令集是否合法
     *
     * @param blockRequestBody
     *         指令集
     * @return 是否合法，为null则校验通过，其他则失败并返回原因
     */
    public String check(BlockRequestBody blockRequestBody) throws TrustSDKException {
        //TODO 此处可能需要校验publicKey的合法性
        if (blockRequestBody == null || blockRequestBody.getBlockBody() == null || StrUtil.isEmpty(blockRequestBody
                .getPublicKey())) {
            return "请求参数缺失";
        }
        List<Transaction> transactions = blockRequestBody.getBlockBody().getTransactions();
        if (CollectionUtil.isEmpty(transactions)) {
            return "指令信息不能为空";
        }

        for (Transaction transaction : transactions) {
            if (!StrUtil.equals(blockRequestBody.getPublicKey(), transaction.getPublicKey())) {
                return "指令内公钥和传来的公钥不匹配";
            }
            if (!transactionService.checkSign(transaction)) {
                return "签名校验不通过";
            }
            if (!transactionService.checkHash(transaction)) {
                return "Hash校验不通过";
            }
        }

//        if (!permissionManager.checkPermission(instructions)) {
//            return "权限校验不通过";
//        }

        return null;
    }

    /**
     * 检验交易集是否合法
     */
    public String checkTx(BlockRequestBodyTx blockRequestBodyTx) throws TrustSDKException{
        if (blockRequestBodyTx==null||blockRequestBodyTx.getBlockBodyTX()==null||
        StrUtil.isEmpty(blockRequestBodyTx.getPublicKey())){
            return "请求参数缺失";
        }
        List<Transaction> transactions=blockRequestBodyTx.getBlockBodyTX().getTransactions();
        if (CollectionUtil.isEmpty(transactions))
            return "交易内容不能为空";
        //TODO 对每个交易进行校验，适当加入逻辑检测等等拓展内容
//        for (Transaction transaction : transactions) {
//
//        }
        return  null;

    }

    /**
     * 添加新的区块
     * @param blockRequestBody blockRequestBody
     * @return Block
     */
    public Block addBlock(BlockRequestBody blockRequestBody) {
        com.mindata.blockchain.block.BlockBody blockBody = blockRequestBody.getBlockBody();
        List<Transaction> transactions = blockBody.getTransactions();
        List<String> hashList = transactions.stream().map(Transaction::getHash).collect(Collectors
                .toList());

        BlockHeader blockHeader = new BlockHeader();
        blockHeader.setHashList(hashList);

        //计算所有指令的hashRoot
        blockHeader.setHashMerkleRoot(new MerkleTree(hashList).build().getRoot());
        blockHeader.setPublicKey(blockRequestBody.getPublicKey());
        blockHeader.setTimeStamp(CommonUtil.getNow());
        blockHeader.setVersion(version);
        blockHeader.setNumber(dbBlockManager.getLastBlockNumber() + 1);
        blockHeader.setHashPreviousBlock(dbBlockManager.getLastBlockHash());
        Block block = new Block();
        block.setBlockBody(blockBody);
        block.setBlockHeader(blockHeader);
        block.setHash(Sha256.sha256(blockHeader.toString() + blockBody.toString()));

        BlockPacket blockPacket = new PacketBuilder<>().setType(PacketType.GENERATE_BLOCK_REQUEST).setBody(new
                RpcBlockBody(block)).build();

        //广播给其他人做验证
        packetSender.sendGroup(blockPacket);

        return block;
    }

    public BlockBodyTX addTxBlock(BlockRequestBodyTx blockRequestBodyTx) {
        BlockBodyTX blockBodyTX=blockRequestBodyTx.getBlockBodyTX();
        List<Transaction>  transactions=blockBodyTX.getTransactions();
        List<String> hasList=transactions.stream().map(Transaction::getHash).collect(Collectors.toList());

        BlockHeader blockHeader=new BlockHeader();
        blockHeader.setHashList(hasList);
        blockHeader.setHashMerkleRoot(new MerkleTree(hasList).build().getRoot());
        blockHeader.setPublicKey(blockRequestBodyTx.getPublicKey());
        blockHeader.setVersion(version);
        blockHeader.setNumber(dbBlockManager.getLastBlockNumber()+1);
        blockHeader.setHashPreviousBlock(dbBlockManager.getLastBlockHash());
        BlockTX blockTX=new BlockTX();
        blockTX.setBlockHeader(blockHeader);

        BlockPacket blockPacket=new PacketBuilder<>().setType(PacketType.GENERATE_BLOCK_REQUEST).setBody(new
                RpcBlockBody(blockTX)).build();
        packetSender.sendGroup(blockPacket);
        return blockBodyTX;





    }

}
