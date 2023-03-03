package com.awakeyo.event.enhance;

import com.awakeyo.event.model.IEvent;
import javassist.ClassClassPath;
import javassist.ClassPool;

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
        // Tomcat有公共资源的classloader来加载class,而javassist使用默认的加载器,搜索不到公共目录下IEventReceiver.class IEvent.class
         Class[] classArray = new Class<?>[]{
                IEventReceiver.class,
                IEvent.class
        };

        ClassPool classPool = ClassPool.getDefault();

        for (Class clazz : classArray) {
            //如果类加载器
            if (classPool.find(clazz.getCanonicalName()) == null) {
                ClassClassPath classPath = new ClassClassPath(clazz);
                //注册一个目录作为类搜索路径
                classPool.insertClassPath(classPath);
            }
        }
    }

}
