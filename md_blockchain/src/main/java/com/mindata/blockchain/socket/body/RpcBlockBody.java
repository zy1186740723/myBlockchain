package com.mindata.blockchain.socket.body;

import com.mindata.blockchain.block.Block;
import com.mindata.blockchain.block.BlockTX;

/**
 * body里是一个block信息
 * @author wuweifeng wrote on 2018/3/12.
 */
public class RpcBlockBody extends BaseBody {
    /**
     * blockJson
     */
    private Block block;
    private BlockTX blockTX;

    public RpcBlockBody() {
        super();
    }

    public RpcBlockBody(Block block) {
        super();
        this.block = block;
    }

    @Override
    public String toString() {
        return "RpcBlockBody{" +
                "block=" + block +
                ", blockTX=" + blockTX +
                '}';
    }

    public RpcBlockBody(BlockTX blockTX){
        super();
        this.blockTX=blockTX;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public BlockTX getBlockTX() {
        return blockTX;
    }

    public void setBlockTX(BlockTX blockTX) {
        this.blockTX = blockTX;
    }
}
