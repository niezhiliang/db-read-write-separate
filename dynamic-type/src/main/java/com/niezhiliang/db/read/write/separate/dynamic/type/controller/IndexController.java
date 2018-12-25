package com.niezhiliang.db.read.write.separate.dynamic.type.controller;

import com.niezhiliang.db.read.write.separate.dynamic.type.domain.User;
import com.niezhiliang.db.read.write.separate.dynamic.type.domain.UserExample;
import com.niezhiliang.db.read.write.separate.dynamic.type.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/25 上午9:47
 */
@RestController
@RequestMapping(value = "/")
public class IndexController {
    @Autowired
    private UserMapper userMapper;


    @RequestMapping(value = "add")
    public Object add() {
        User user = new User();
        user.setAge(22);
        user.setName("苏雨");
        user.setSex("男");
        return userMapper.insertSelective(user);
    }

    @RequestMapping(value = "get")
    public Object index() {
        UserExample example = new UserExample();
        example.createCriteria().andIdIsNotNull();
        return userMapper.selectByExample(example);
    }

    @RequestMapping(value = "update")
    public Object update() {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo("苏雨");
        User user = userMapper.selectByExample(userExample).get(0);
        user.setAge(120);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @RequestMapping(value = "delete")
    public Object delete() {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo("苏雨");
        return userMapper.deleteByPrimaryKey(userMapper.selectByExample(userExample).get(0).getId());
    }
}
