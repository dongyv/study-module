package application.smartframe.helper;

import application.smartframe.annotation.Inject;
import application.smartframe.util.ArrayUtil;
import application.smartframe.util.CollectionUtil;
import application.smartframe.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by xiachenhang on 2018/12/8
 */
public class IocHelpter {

    static {
        //获取所有的 Bean类 与 Bean实例 之间的映射关系（简称Bean Map）
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();

        if(CollectionUtil.isNotEmpty(beanMap)){
            for(Map.Entry<Class<?>,Object>beanEntry : beanMap.entrySet()){
                //从 BeanMap 获取Bean类的实例
                Class<?>beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();

                Field[] beanFields = beanClass.getDeclaredFields();

                if(ArrayUtil.isNotEmpty(beanFields)){

                    for(Field beanField : beanFields){
                        //判断是否带有 Inject 的注解
                        if(beanField.isAnnotationPresent(Inject.class)){
                            //在映射中 获取 BeanField 实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);

                            if(beanFieldInstance != null){
                                //反射 初始化beanField的实例
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                            }
                        }

                    }
                }
            }
        }
    }

}
