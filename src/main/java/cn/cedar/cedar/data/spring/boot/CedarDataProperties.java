package cn.cedar.cedar.data.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author cedar12.zxd@qq.com
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

  private boolean displaySql;

  private int maxLayer;

  public String getScanPackage() {
    return scanPackage;
  }

  public void setScanPackage(String scanPackage) {
    this.scanPackage = scanPackage;
  }

  public boolean isDisplaySql() {
    return displaySql;
  }

  public void setDisplaySql(boolean displaySql) {
    this.displaySql = displaySql;
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
            ", dispalySql=" + displaySql +
            ", maxLayer=" + maxLayer +
            '}';
  }

  private static String humpToLine(String para){
    StringBuilder sb=new StringBuilder(para);
    int temp=0;
    if (!para.contains("-")) {
      for(int i=0;i<para.length();i++){
        if(Character.isUpperCase(para.charAt(i))){
          sb.insert(i+temp, "-");
          temp+=1;
        }
      }
    }
    return sb.toString().toLowerCase();
  }

  private static Object toTypeValue(Class<?> cls,String value){
    if(cls==String.class){
      return value.trim();
    }
    if(cls==int.class||cls==Integer.class){
      return Integer.parseInt(value);
    }
    if(cls==boolean.class||cls== Boolean.class){
      return Boolean.parseBoolean(value);
    }
    return null;
  }

  private void init(){
    Field[] fields=this.getClass().getDeclaredFields();
    for (Field field : fields) {
      String name=field.getName();
      String value=this.environment.getProperty(CEDAR_DATA_PREFIX+"."+name);
      if(value==null){
        value=this.environment.getProperty(CEDAR_DATA_PREFIX+"."+humpToLine(name));
      }
      if(value!=null){
        field.setAccessible(true);
        try {
          field.set(this,toTypeValue(field.getType(),value));
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
        field.setAccessible(false);
      }
    }

  }


  @Override
  public void setEnvironment(Environment environment) {
    this.environment=environment;
    this.init();
  }

  public Environment getEnvironment() {
    return environment;
  }
}