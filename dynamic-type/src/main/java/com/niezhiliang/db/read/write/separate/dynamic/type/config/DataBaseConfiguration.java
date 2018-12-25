package com.niezhiliang.db.read.write.separate.dynamic.type.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/24 下午4:16
 */
@Configuration
public class DataBaseConfiguration {

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Bean(name="writeDataSource", destroyMethod = "close", initMethod="init")
    @Primary
    @ConfigurationProperties(prefix = "spring.write.datasource")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create()
                .type(dataSourceType).build();
    }

    @Bean(name = "readDataSourceOne")
    @ConfigurationProperties(prefix = "spring.read.one.datasource")
    public DataSource readDataSourceOne() {
        return DataSourceBuilder.create()
                .type(dataSourceType).build();
    }

    @Bean(name = "readDataSourceTwo")
    @ConfigurationProperties(prefix = "spring.read.two.datasource")
    public DataSource readDataSourceTwo() {
        return DataSourceBuilder.create()
                .type(dataSourceType).build();
    }

    @Bean("readDataSources")
    public List<DataSource> readDataSources() {
        List<DataSource> dataSources = new ArrayList<DataSource>();
        dataSources.add(readDataSourceOne());
        dataSources.add(readDataSourceTwo());
        return dataSources;
    }
}
