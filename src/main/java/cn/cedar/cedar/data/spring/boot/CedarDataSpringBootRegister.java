package cn.cedar.cedar.data.spring.boot;

import cn.cedar.data.InstanceFactory;
import cn.cedar.data.JdbcManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author cedar12 413338772@qq.com
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
        InstanceFactory.setJdbcManager(new JdbcTemplatelManager(cedarDataJdbcTemplate));
    }

    private static final String EVN="cedar-data-spring-boot-starter";

    public CedarDataSpringBootRegister(){
    }

    public CedarDataSpringBootRegister(CedarDataProperties properties) {
        InstanceFactory.setEnv(EVN);
        this.properties=properties;
        this.basePackage=properties.getEnvironment().getProperty(CedarDataProperties.SCAN_PACKAGE).split(",");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry=registry;
        if (basePackage==null||basePackage.length==0) {
            return;
        }

        String maxLayer=properties.getEnvironment().getProperty(CedarDataProperties.MAX_LAYER);
        if(maxLayer!=null){
            InstanceFactory.setMaxLayer(Integer.parseInt(maxLayer));
        }
        String displaySql=properties.getEnvironment().getProperty(CedarDataProperties.DISPLAY_SQL);
        if(displaySql!=null){
            JdbcTemplatelManager.displaySql=Boolean.parseBoolean(displaySql);
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