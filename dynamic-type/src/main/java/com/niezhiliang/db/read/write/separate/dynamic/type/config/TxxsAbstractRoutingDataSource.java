package com.niezhiliang.db.read.write.separate.dynamic.type.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/24 下午4:52
 */
public class TxxsAbstractRoutingDataSource extends AbstractRoutingDataSource {

    private  int dataSourceNumber;

    private AtomicInteger count = new AtomicInteger(0);


    public TxxsAbstractRoutingDataSource(int dataSourceNumber) {
        this.dataSourceNumber = dataSourceNumber;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String typeKey = DataSourceContextHolder.getJdbcType();
        if (typeKey.equals(DataSourceType.write.getType()))
            return DataSourceType.write.getType();
        // 读 简单负载均衡
        int number = count.getAndAdd(1);
        int lookupKey = number % dataSourceNumber;
        return new Integer(lookupKey);
    }
}
