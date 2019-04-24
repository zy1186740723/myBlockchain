package com.mindata.blockchain.core.requestbody;

import com.mindata.blockchain.block.BlockBodyTX;

/**
 * @Author: zhangyan
 * @Date: 2019/4/15 15:10
 * @Version 1.0
 */
public class BlockRequestBodyTx {
    //构建节点公钥
    private String publicKey;
    private BlockBodyTX blockBodyTX;

    @Override
    public String toString() {
        return "BlockRequestBodyTx{" +
                "publicKey='" + publicKey + '\'' +
                ", blockBodyTX=" + blockBodyTX +
                '}';
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public BlockBodyTX getBlockBodyTX() {
        return blockBodyTX;
    }

    public void setBlockBodyTX(BlockBodyTX blockBodyTX) {
        this.blockBodyTX = blockBodyTX;
    }
}
