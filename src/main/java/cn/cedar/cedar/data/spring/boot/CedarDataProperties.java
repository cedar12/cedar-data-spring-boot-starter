package cn.cedar.cedar.data.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author cedar12 413338772@qq.com
 */
@Component
@ConfigurationProperties(prefix = CedarDataProperties.CEDAR_DATA_PREFIX,ignoreUnknownFields = true)
public class CedarDataProperties implements EnvironmentAware {
  public static final String CEDAR_DATA_PREFIX = "cedar.data";
  public static final String SCAN_PACKAGE=CEDAR_DATA_PREFIX+".scan-package";
  public static final String MAX_LAYER=CEDAR_DATA_PREFIX+".max-layer";
  public static final String DISPLAY_SQL=CEDAR_DATA_PREFIX+".display-sql";

  private Environment environment;

  private String scanPackage;

  private boolean dispalySql;

  private int maxLayer;

  public String getScanPackage() {
    return scanPackage;
  }

  public void setScanPackage(String scanPackage) {
    this.scanPackage = scanPackage;
  }

  public boolean isDispalySql() {
    return dispalySql;
  }

  public void setDispalySql(boolean dispalySql) {
    this.dispalySql = dispalySql;
  }

  public int getMaxLayer() {
    return maxLayer;
  }

  public void setMaxLayer(int maxLayer) {
    this.maxLayer = maxLayer;
  }

  @Override
  public String toString() {
    return "{" +
            "scanPackage='" + scanPackage + '\'' +
            ", dispalySql=" + dispalySql +
            ", maxLayer=" + maxLayer +
            '}';
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment=environment;
  }

  public Environment getEnvironment() {
    return environment;
  }
}