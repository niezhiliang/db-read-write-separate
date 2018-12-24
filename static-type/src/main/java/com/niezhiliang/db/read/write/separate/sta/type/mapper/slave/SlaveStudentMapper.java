package com.niezhiliang.db.read.write.separate.sta.type.mapper.slave;

import com.niezhiliang.db.read.write.separate.sta.type.domain.SlaveStudent;
import com.niezhiliang.db.read.write.separate.sta.type.domain.SlaveStudentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SlaveStudentMapper {
    long countByExample(SlaveStudentExample example);

    int deleteByExample(SlaveStudentExample example);

    int deleteByPrimaryKey(Integer stuid);

    int insert(SlaveStudent record);

    int insertSelective(SlaveStudent record);

    List<SlaveStudent> selectByExample(SlaveStudentExample example);

    SlaveStudent selectByPrimaryKey(Integer stuid);

    int updateByExampleSelective(@Param("record") SlaveStudent record, @Param("example") SlaveStudentExample example);

    int updateByExample(@Param("record") SlaveStudent record, @Param("example") SlaveStudentExample example);

    int updateByPrimaryKeySelective(SlaveStudent record);

    int updateByPrimaryKey(SlaveStudent record);
}