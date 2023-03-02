package com.awakeyo.event.enhance;

import com.awakeyo.event.model.IEvent;

/**
 * @version : 1.0
 * @ClassName: EnhanceUtils
 * @Description: 增强工具类
 * @Auther: awake
 * @Date: 2022/10/31 15:37
 **/
public class EnhanceUtils {
    static {
        // 适配Tomcat，因为Tomcat不是用的默认的类加载器，而Javaassist用的是默认的加载器
         Class[] classArray = new Class<?>[]{
                IEventReceiver.class,
                IEvent.class
        };

        Class[] classPool = ClassPool.getDefault();

        for (var clazz : classArray) {
            if (classPool.find(clazz.getCanonicalName()) == null) {
                ClassClassPath classPath = new ClassClassPath(clazz);
                classPool.insertClassPath(classPath);
            }
        }
    }

}
