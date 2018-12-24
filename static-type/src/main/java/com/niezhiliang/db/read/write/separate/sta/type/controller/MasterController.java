package com.niezhiliang.db.read.write.separate.sta.type.controller;

import com.niezhiliang.db.read.write.separate.sta.type.domain.MasterUserExample;
import com.niezhiliang.db.read.write.separate.sta.type.mapper.master.MasterUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/24 上午10:15
 */
@RestController
@RequestMapping(value = "master")
public class MasterController {

    @Autowired
    private MasterUserMapper userMapper;

    @RequestMapping(value = "users")
    public Object users() {
        MasterUserExample userExample = new MasterUserExample();
        userExample.createCriteria().andIdIsNotNull();

        return userMapper.selectByExample(userExample);
    }
}
