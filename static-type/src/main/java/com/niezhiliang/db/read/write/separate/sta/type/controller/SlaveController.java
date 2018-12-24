package com.niezhiliang.db.read.write.separate.sta.type.controller;

import com.niezhiliang.db.read.write.separate.sta.type.domain.SlaveStudentExample;
import com.niezhiliang.db.read.write.separate.sta.type.domain.SlaveUserExample;
import com.niezhiliang.db.read.write.separate.sta.type.mapper.slave.SlaveStudentMapper;
import com.niezhiliang.db.read.write.separate.sta.type.mapper.slave.SlaveUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/24 上午10:20
 */
@RestController
@RequestMapping(value = "slave")
public class SlaveController {
    @Autowired
    private SlaveUserMapper userMapper;
    @Autowired
    private SlaveStudentMapper studentMapper;

    @RequestMapping(value = "users")
    public Object users() {
        SlaveUserExample userExample = new SlaveUserExample();
        userExample.createCriteria().andIdIsNotNull();

        return userMapper.selectByExample(userExample);
    }

    @RequestMapping(value = "students")
    public Object students() {
        SlaveStudentExample studentExample = new SlaveStudentExample();
        studentExample.createCriteria().andStuidIsNotNull();

        return studentMapper.selectByExample(studentExample);
    }


}
