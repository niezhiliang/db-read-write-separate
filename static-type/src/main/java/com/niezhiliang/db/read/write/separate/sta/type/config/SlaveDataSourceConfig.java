package com.niezhiliang.db.read.write.separate.sta.type.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/24 上午9:25
 */
@Configuration
@MapperScan(basePackages = "com.niezhiliang.db.read.write.separate.sta.type.mapper.slave",sqlSessionFactoryRef="slaveSqlSessionFactory")
public class SlaveDataSourceConfig {

    @Value(value = "${slave.datasource.url}")
    private String url;

    @Value(value = "${slave.datasource.username}")
    private String userName;

    @Value(value = "${slave.datasource.password}")
    private String password;

    @Value(value = "${slave.datasource.driver-class-name}")
    private String driverClass;

    @Value(value = "${slave.datasource.mapper-locations}")
    private String mapperLocations;

    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClass);
        return dataSource;
    }


    @Bean(name = "slaveTransactionManager")
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(slaveDataSource());
    }

    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("slaveDataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations));
        return sessionFactory.getObject();
    }
}
