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

import cn.cedar.data.spring.boot.factory.CedarDataSpringProxyFactory;
import cn.cedar.data.annotation.CedarData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Set;

/**
 * @author cedar12.zxd@qq.com
 */
class CedarDataBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    private static Log log= LogFactory.getLog(CedarDataBeanDefinitionScanner.class);

    private static final String INTERFACE_CLASS="interfaceClass";

    private String[] basePackage;

    public CedarDataBeanDefinitionScanner(BeanDefinitionRegistry registry, String[] basePackage) {
        super(registry, false);
        this.basePackage=basePackage;
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        this.addFilter();
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            log.error("@CedarData interface is empty");
            return beanDefinitionHolders;
        }
        this.createBeanDefinition(beanDefinitionHolders);
        return beanDefinitionHolders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isInterface() && metadata.isIndependent();
    }

    private void addFilter() {
        addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className=metadataReader.getClassMetadata().getClassName();
                try {
                    Class<?> cls= Class.forName(className);
                    CedarData cedarData=cls.getAnnotation(CedarData.class);
                    if(cedarData!=null){
                        return true;
                    }
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage());
                }
                return false;
            }
        });
    }

    private void createBeanDefinition(Set<BeanDefinitionHolder> beanDefinitionHolders) {
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            GenericBeanDefinition definition = ((GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition());
            log.info(definition.getBeanClassName());
            definition.getPropertyValues().add(INTERFACE_CLASS, definition.getBeanClassName());
            definition.setBeanClass(CedarDataSpringProxyFactory.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

}