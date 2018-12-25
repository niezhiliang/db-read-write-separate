package com.niezhiliang.db.read.write.separate.dynamic.type.aop;

import com.niezhiliang.db.read.write.separate.dynamic.type.config.DataSourceContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/24 下午5:25
 */
@Aspect
@Component
public class DataSourceAop {

    public static final Logger logger = LoggerFactory.getLogger(DataSourceAop.class);

    @Before("execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*.select*(..)) or execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*..count*(..))")
    public void setReadDataSourceType() {
        DataSourceContextHolder.read();
        logger.info("dataSource切换到：Read");
    }

    @Before(("execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*.insert*(..)) or execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*.update*(..)) or execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*.delete*(..))"))
    public void setWriteDataSourceType() {
        DataSourceContextHolder.write();
        logger.info("dataSource切换到:Write");
    }
}
