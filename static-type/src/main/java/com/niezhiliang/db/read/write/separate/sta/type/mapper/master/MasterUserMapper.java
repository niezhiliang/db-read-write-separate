package com.niezhiliang.db.read.write.separate.sta.type.mapper.master;

import com.niezhiliang.db.read.write.separate.sta.type.domain.MasterUser;
import com.niezhiliang.db.read.write.separate.sta.type.domain.MasterUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MasterUserMapper {
    long countByExample(MasterUserExample example);

    int deleteByExample(MasterUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MasterUser record);

    int insertSelective(MasterUser record);

    List<MasterUser> selectByExample(MasterUserExample example);

    MasterUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MasterUser record, @Param("example") MasterUserExample example);

    int updateByExample(@Param("record") MasterUser record, @Param("example") MasterUserExample example);

    int updateByPrimaryKeySelective(MasterUser record);

    int updateByPrimaryKey(MasterUser record);
}