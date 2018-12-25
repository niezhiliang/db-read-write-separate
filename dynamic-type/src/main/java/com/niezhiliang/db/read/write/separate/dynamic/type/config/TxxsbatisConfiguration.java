package com.niezhiliang.db.read.write.separate.dynamic.type.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/24 下午4:43
 */
@Configuration
@ConditionalOnClass({EnableTransactionManagement.class})
@MapperScan(basePackages = {"com.niezhiliang.db.read.write.separate.dynamic.type.mapper"})
public class TxxsbatisConfiguration {

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Value("${spring.datasource.readSize}")
    private String dataSourceSize;

    @Resource(name = "writeDataSource")
    private DataSource dataSource;

    @Resource(name = "readDataSources")
    private List<DataSource> readDataSources;


    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(roundRobinDataSourceProxy());
        sqlSessionFactoryBean.setTypeAliasesPackage("com.niezhiliang.db.read.write.separate.dynamic.type.domain");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources("classpath:mappers*//*.xml"));
        sqlSessionFactoryBean.getObject().getConfiguration()
                .setMapUnderscoreToCamelCase(true);

        return sqlSessionFactoryBean.getObject();
    }


    @Bean
    public AbstractRoutingDataSource roundRobinDataSourceProxy() {
        int size = Integer.parseInt(dataSourceSize);
        TxxsAbstractRoutingDataSource proxy = new TxxsAbstractRoutingDataSource(size);
        Map<Object,Object> targetDataSourceSize = new HashMap<Object,Object>();
        targetDataSourceSize.put(dataSourceType.getTypeName(),dataSource);

        for (int i = 0; i < size; i++) {
            targetDataSourceSize.put(i,readDataSources.get(i));
        }
        proxy.setDefaultTargetDataSource(dataSource);
        proxy.setTargetDataSources(targetDataSourceSize);

        return proxy;
    }


}
