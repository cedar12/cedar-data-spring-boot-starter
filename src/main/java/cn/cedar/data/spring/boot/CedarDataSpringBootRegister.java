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
import cn.cedar.data.JdbcManager;
import cn.cedar.data.expcetion.CedarDataRuntimeException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;

/**
 * @author cedar12.zxd@qq.com
 */
public class CedarDataSpringBootRegister implements BeanDefinitionRegistryPostProcessor {
    private String[] basePackage;
    private CedarDataProperties properties;

    private ApplicationContext ctx;
    private JdbcTemplate cedarDataJdbcTemplate;
    private BeanDefinitionRegistry registry;


    @Autowired
    public void setProperties(CedarDataProperties properties) {
        this.properties = properties;
        this.basePackage=properties.getScanPackage().split(",");
    }

    @Autowired
    public void setCedarDataJdbcTemplate(JdbcTemplate cedarDataJdbcTemplate) {
        this.cedarDataJdbcTemplate = cedarDataJdbcTemplate;
        InstanceFactory.setJdbcManager(new JdbcTemplateManager(cedarDataJdbcTemplate));
    }

    private static final String EVN="cedar-data-spring-boot-starter";

    public CedarDataSpringBootRegister(){
    }

    public CedarDataSpringBootRegister(CedarDataProperties properties) {
        InstanceFactory.setEnv(EVN);
        this.properties=properties;
        if(properties.getScanPackage()==null){
            throw new CedarDataRuntimeException(" cedar.data.scan-package or cedar.data.scanPackage is null");
        }
        this.basePackage = properties.getScanPackage().split(",");
        if(this.basePackage!=null&&this.basePackage.length==0){
            throw new CedarDataRuntimeException(" cedar.data.scan-package or cedar.data.scanPackage is null");
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry=registry;
        if(properties.getMaxLayer()!=0){
            InstanceFactory.setMaxLayer(properties.getMaxLayer());
        }
        if(properties.isDisplaySql()){
            JdbcTemplateManager.displaySql=properties.isDisplaySql();
        }
        CedarDataBeanDefinitionScanner scanner = new CedarDataBeanDefinitionScanner(registry,basePackage);
        scanner.doScan(basePackage);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    public static class PreloadJdbcManger extends JdbcManager {
        @Override
        public Connection getConnection() {
            return null;
        }
    }

}