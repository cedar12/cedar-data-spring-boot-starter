/**
 *	  Copyright 2020 cedar12.zxd@qq.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.cedar.data.spring.boot;

import cn.cedar.data.InstanceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


/**
 * @author cedar12.zxd@qq.com
 */
@ComponentScan("cn.cedar.data.spring.boot")
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