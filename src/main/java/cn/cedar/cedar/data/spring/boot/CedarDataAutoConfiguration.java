package cn.cedar.cedar.data.spring.boot;

import cn.cedar.data.InstanceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


/**
 * @author cedar12.zxd@qq.com
 */
@ComponentScan("cn.cedar.cedar.data.spring.boot")
@EnableConfigurationProperties({CedarDataProperties.class})
@Configuration
@ConditionalOnClass(JdbcTemplate.class)
public class CedarDataAutoConfiguration {

    @DependsOn("cedarDataProperties")
    @Bean
    public static CedarDataSpringBootRegister cedarDataSpringBootRegister(CedarDataProperties cedarDataProperties){
        return new CedarDataSpringBootRegister(cedarDataProperties);
    }


    @Primary
    @DependsOn("dataSource")
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
        InstanceFactory.setJdbcManager(new JdbcTemplateManager(jdbcTemplate));
        return jdbcTemplate;
    }

}