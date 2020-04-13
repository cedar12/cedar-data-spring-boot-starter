# Cedar Data Spring Boot Starter
一个易于使用的cedar-data(1.1.8+)框架的Spring Boot集成组件

依赖
```xml
<dependency>
  <groupId>com.github.cedar12</groupId>
  <artifactId>cedar-data-spring-boot-starter</artifactId>
  <version>1.1.8</version>
</dependency>
```

配置
```yaml
cedar:
    data: 
      scan-package: xx.xx.xx # ,分隔多个包
      display-sql: false # 是否日志输出sql 默认false
      max-layer: 5 #动态方法sql文件import导入的最大层数 默认5
```

数据源
```java
@Configuration
public class DataSourceConfig{

    @ConfigurationProperties(prefix="spring.datasource.druid")
    @Bean
    public DataSource dataSource(){
        return new DruidDataSource();  
    }

}
```


事务开启使用Spring的 `@EnableTransactionManager` `@Transactional`注解