## 数据库读写分离

数据库读写分离环境搭建：https://github.com/niezhiliang/mysql-master-slave-docker


数据库读写分离一般分为两种，一种是静态的，一种是动态的，顾名思义静态是配置了不会变，有局限性，而动态的会根据业务的需求自动切换数据源

### 静态

这种方式一般适用于项目需要依赖两个不同的数据库，而不是所谓的读写分离的主从数据库。比如一个数据库没有用户表，他想用自己另一个项目中的
用户，这个时候用这种方式就比较合适啦。要想用这种方式来配置数据库的读写分离也不是不行，那就把读写业务拆分出来嘛。 生成两套xml和mapper.java, 一套用来处理
写操作，另一套用来进行读操作。功能能实现，但在写代码的时候就特别烦，有时候读的业务写到写的数据库就尴尬啦。

这种方式比较简单，就不做说明啦，相信我自己也不会忘的。😝



### 动态

这种方式就比较适用于数据库的读写分离啦。这种方式解决了静态方式的缺陷，共用一套mapper文件，无需拆分读写业务，用到aop对mapper的方法进行分析，判断是读操作还是写操作，
并切换到相应的数据源。架构搭建好以后，编码的时候无需关心什么读跟写，只需专注于业务实现就可以啦。

- 配置好所有的读写数据源
    
    ```java
    @Configuration
    public class DataBaseConfiguration {
    
        @Value("${spring.datasource.type}")
        private Class<? extends DataSource> dataSourceType;
    
        //这个就是我们的写数据源，也就是master库
        @Bean(name="writeDataSource", destroyMethod = "close", initMethod="init")
        @Primary
        @ConfigurationProperties(prefix = "spring.write.datasource")
        public DataSource writeDataSource() {
            return DataSourceBuilder.create()
                    .type(dataSourceType).build();
        }
        
        //配置第一个读库 所有读数据源都需要将其配置成bean 并添加到读数据源集合中
        @Bean(name = "readDataSourceOne")
        @ConfigurationProperties(prefix = "spring.read.one.datasource")
        public DataSource readDataSourceOne() {
            return DataSourceBuilder.create()
                    .type(dataSourceType).build();
        }
        
        //配置第二个读库
        @Bean(name = "readDataSourceTwo")
        @ConfigurationProperties(prefix = "spring.read.two.datasource")
        public DataSource readDataSourceTwo() {
            return DataSourceBuilder.create()
                    .type(dataSourceType).build();
        }
    
        //读写库集合的Bean
        @Bean("readDataSources")
        public List<DataSource> readDataSources() {
            List<DataSource> dataSources = new ArrayList<DataSource>();
            dataSources.add(readDataSourceOne());
            dataSources.add(readDataSourceTwo());
            return dataSources;
        }
    }
    ```
    
- 配置动态的sqlSessionFactory

```java
@Configuration
@ConditionalOnClass({EnableTransactionManagement.class})
@MapperScan(basePackages = {"com.niezhiliang.db.read.write.separate.dynamic.type.mapper"})
public class TxxsbatisConfiguration {
    
    //我们这里使用的是alibaba druid连接池
    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    //读取配置的读数据库的数量（从库）
    @Value("${spring.datasource.readSize}")
    private String dataSourceSize;

    //我们的写数据库，也就是主数据库
    @Resource(name = "writeDataSource")
    private DataSource dataSource;
    
    //所有的读数据源集合
    @Resource(name = "readDataSources")
    private List<DataSource> readDataSources;


    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(roundRobinDataSourceProxy());
        sqlSessionFactoryBean.setTypeAliasesPackage("com.niezhiliang.db.read.write.separate.dynamic.type.domain");
        //mapper.xml文件位置
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources("classpath:mappers*//*.xml"));
        sqlSessionFactoryBean.getObject().getConfiguration()
                .setMapUnderscoreToCamelCase(true);

        return sqlSessionFactoryBean.getObject();
    }


    //配置动态数据源的路由对象
    @Bean
    public AbstractRoutingDataSource roundRobinDataSourceProxy() {
        TxxsAbstractRoutingDataSource proxy = new TxxsAbstractRoutingDataSource(readDataSources.size());
        Map<Object,Object> targetDataSourceSize = new HashMap<Object,Object>();
        targetDataSourceSize.put(dataSourceType.getTypeName(),dataSource);

        for (int i = 0; i < readDataSources.size(); i++) {
            targetDataSourceSize.put(i,readDataSources.get(i));
        }
        //如果没有读数据集 则默认为写数据库
        proxy.setDefaultTargetDataSource(dataSource);
        //配置读数据源
        proxy.setTargetDataSources(targetDataSourceSize);

        return proxy;
    }

}
```

- 配置一个线程池，用于切换数据源(通过改变当前线程变量的方法，达到动态改变数据源)

```java
public class DataSourceContextHolder {
    private static final ThreadLocal<String> local = new ThreadLocal<String>();

    public static ThreadLocal<String> getLocal() {
        return local;
    }

    /**
     * 读可能是多个库
     */
    public static void read() {
        local.set(DataSourceType.read.getType());
    }

    /**
     * 写只有一个库
     */
    public static void write() {
        local.set(DataSourceType.write.getType());
    }

    public static String getJdbcType() {
        return local.get();
    }
}
```

- aop拦截所有的数据库请求

```java
@Aspect
@Component
public class DataSourceAop {

    public static final Logger logger = LoggerFactory.getLogger(DataSourceAop.class);
    
    //拦截mapper所有的读操作，将数据源动态切换到读数据源
    @Before("execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*.select*(..)) or execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*..count*(..))")
    public void setReadDataSourceType() {
        DataSourceContextHolder.read();
        logger.info("dataSource切换到：Read");
    }
    //拦截mapper所有的写操作，将数据源动态切换到写数据源
    @Before(("execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*.insert*(..)) or execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*.update*(..)) or execution(* com.niezhiliang.db.read.write.separate.dynamic.type.mapper..*.delete*(..))"))
    public void setWriteDataSourceType() {
        DataSourceContextHolder.write();
        logger.info("dataSource切换到:Write");
    }
}
```

- 动态数据源切换 

```java
public class TxxsAbstractRoutingDataSource extends AbstractRoutingDataSource {

    private  int dataSourceNumber;

    private AtomicInteger count = new AtomicInteger(0);


    public TxxsAbstractRoutingDataSource(int dataSourceNumber) {
        this.dataSourceNumber = dataSourceNumber;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        //获取当前线程是进行读还是写
        String typeKey = DataSourceContextHolder.getJdbcType();
        if (typeKey.equals(DataSourceType.write.getType()))
            return DataSourceType.write.getType();
        // 读 简单负载均衡 count会一直累加，那后模读数据源的数量 达到简单的负载均衡
        int number = count.getAndAdd(1);
        int lookupKey = number % dataSourceNumber;
        return new Integer(lookupKey);
    }
}
                  
```        