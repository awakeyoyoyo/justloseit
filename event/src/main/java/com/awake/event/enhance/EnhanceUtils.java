package com.awake.event.enhance;

import com.awake.event.model.IEvent;
import com.awake.util.IdUtils;
import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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


    public static IEventReceiver createEventReceiver(EventReceiverDefinition definition) throws NotFoundException, CannotCompileException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ClassPool classPool = ClassPool.getDefault();

        Object bean = definition.getBean();
        Method method = definition.getMethod();
        Class<?> clazz = definition.getEventClazz();

        // 定义类名称
        CtClass enhanceClazz = classPool.makeClass(EnhanceUtils.class.getCanonicalName() + "event" + IdUtils.getLocalIntId());
        enhanceClazz.addInterface(classPool.get(IEventReceiver.class.getCanonicalName()));

        // 定义类中的一个成员
        CtField field = new CtField(classPool.get(bean.getClass().getCanonicalName()), "bean", enhanceClazz);
        field.setModifiers(Modifier.PRIVATE);
        enhanceClazz.addField(field);

        // 定义类的构造器
        CtConstructor constructor = new CtConstructor(classPool.get(new String[]{bean.getClass().getCanonicalName()}), enhanceClazz);
        constructor.setBody("{this.bean=$1;}");
        constructor.setModifiers(Modifier.PUBLIC);
        enhanceClazz.addConstructor(constructor);

        // 定义类实现的接口方法
        CtMethod invokeMethod = new CtMethod(classPool.get(void.class.getCanonicalName()), "invoke", classPool.get(new String[]{IEvent.class.getCanonicalName()}), enhanceClazz);
        invokeMethod.setModifiers(Modifier.PUBLIC + Modifier.FINAL);
        String invokeMethodBody = "{this.bean." + method.getName() + "((" + clazz.getCanonicalName() + ")$1);}";// 强制类型转换，转换为具体的Event类型的类型
        invokeMethod.setBody(invokeMethodBody);
        enhanceClazz.addMethod(invokeMethod);

        // 释放缓存
        enhanceClazz.detach();

        Class<IEventReceiver> resultClazz = enhanceClazz.toClass();
        Constructor<IEventReceiver> resultConstructor = resultClazz.getConstructor(bean.getClass());
        return resultConstructor.newInstance(bean);
    }

}
