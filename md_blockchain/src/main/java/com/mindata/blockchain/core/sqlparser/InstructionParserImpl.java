package com.mindata.blockchain.core.sqlparser;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mindata.blockchain.block.Instruction;
import com.mindata.blockchain.block.InstructionBase;
import com.mindata.blockchain.common.FastJsonUtil;
import com.mindata.blockchain.core.model.base.BaseEntity;
import com.mindata.blockchain.core.model.convert.ConvertTableName;

/**
 * 将区块内指令解析并入库
 * @author wuweifeng wrote on 2018/3/21.
 */
@Service
public class InstructionParserImpl<T extends BaseEntity> implements InstructionParser {
    @Resource
    private ConvertTableName<T> convertTableName;
    @Resource
    private AbstractSqlParser<T>[] sqlParsers;

    @Override
    public boolean parse(InstructionBase instructionBase) {
        byte operation = instructionBase.getOperation();
        String table = instructionBase.getTable();
        String json = instructionBase.getOldJson();
        //表对应的类名，如MessageEntity.class
        Class<T> clazz = convertTableName.convertOf(table);
        T object = FastJsonUtil.toBean(json, clazz);
        for (AbstractSqlParser<T> sqlParser : sqlParsers) {
            if (clazz.equals(sqlParser.getEntityClass())) {
            	if(instructionBase instanceof Instruction){
            		object.setPublicKey(((Instruction)instructionBase).getPublicKey());
            	}
                sqlParser.parse(operation, instructionBase.getInstructionId(), object);
                break;
            }
        }

        return true;
    }
}
