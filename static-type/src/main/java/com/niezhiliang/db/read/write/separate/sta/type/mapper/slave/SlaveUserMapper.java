package com.niezhiliang.db.read.write.separate.sta.type.mapper.slave;

import com.niezhiliang.db.read.write.separate.sta.type.domain.SlaveUser;
import com.niezhiliang.db.read.write.separate.sta.type.domain.SlaveUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SlaveUserMapper {
    long countByExample(SlaveUserExample example);

    int deleteByExample(SlaveUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SlaveUser record);

    int insertSelective(SlaveUser record);

    List<SlaveUser> selectByExample(SlaveUserExample example);

    SlaveUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SlaveUser record, @Param("example") SlaveUserExample example);

    int updateByExample(@Param("record") SlaveUser record, @Param("example") SlaveUserExample example);

    int updateByPrimaryKeySelective(SlaveUser record);

    int updateByPrimaryKey(SlaveUser record);
}