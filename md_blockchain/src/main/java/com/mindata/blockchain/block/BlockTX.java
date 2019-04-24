package com.mindata.blockchain.block;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * @Author: zhangyan
 * @Date: 2019/4/15 20:02
 * @Version 1.0
 */
public class BlockTX {
    /**
     * 区块头
     */
    private BlockHeader blockHeader;
    /**
     * 区块body
     */
    private BlockBody blockBody;
    /**
     * 该区块的hash
     */
    private String hash;

    private String calculateHash(){
        return DigestUtil.sha256Hex(blockHeader.toString()+blockBody.toString());
    }

    @Override
    public String toString() {
        return "BlockTX{" +
                "blockHeader=" + blockHeader +
                ", blockBody=" + blockBody +
                ", hash='" + hash + '\'' +
                '}';
    }

    public BlockHeader getBlockHeader() {
        return blockHeader;
    }

    public void setBlockHeader(BlockHeader blockHeader) {
        this.blockHeader = blockHeader;
    }

    public BlockBody getBlockBody() {
        return blockBody;
    }

    public void setBlockBody(BlockBody blockBody) {
        this.blockBody = blockBody;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
