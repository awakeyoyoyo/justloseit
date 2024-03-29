package com.awake.net2.router.receiver;


import com.awake.net2.session.Session;
import com.awake.util.IdUtils;
import com.awake.util.base.StringUtils;
import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @version : 1.0
 * @ClassName: EnHanceUtils
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/9/6 11:56
 **/
public class EnhanceUtils {
    static {
        Class[] classArray = new Class<?>[]{
                IPacketReceiver.class,
                Session.class
        };

        ClassPool classPool = ClassPool.getDefault();

        for (Class clazz : classArray) {
            if (classPool.find(clazz.getCanonicalName()) == null) {
                ClassClassPath classPath = new ClassClassPath(clazz);
                classPool.insertClassPath(classPath);
            }
        }
    }

    public static IPacketReceiver createPacketReceiver(PacketReceiverDefinition definition) throws NotFoundException, CannotCompileException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ClassPool classPool = ClassPool.getDefault();
        Object bean = definition.getBean();
        Method method = definition.getMethod();
        Class<?> packetClazz = definition.getPacketClazz();

        CtClass enhanceClazz = classPool.makeClass(com.awake.event.enhance.EnhanceUtils.class.getCanonicalName() + "Dispatcher" + IdUtils.getLocalIntId());
        enhanceClazz.addInterface(classPool.get(IPacketReceiver.class.getCanonicalName()));

        CtField field = new CtField(classPool.get(bean.getClass().getCanonicalName()), "bean", enhanceClazz);
        field.setModifiers(Modifier.PRIVATE);
        enhanceClazz.addField(field);

        CtConstructor constructor = new CtConstructor(classPool.get(new String[]{bean.getClass().getCanonicalName()}), enhanceClazz);
        constructor.setBody("{this.bean=$1;}");
        constructor.setModifiers(Modifier.PUBLIC);
        enhanceClazz.addConstructor(constructor);

        CtMethod invokeMethod = new CtMethod(classPool.get(void.class.getCanonicalName()), "invoke", classPool.get(new String[]{Session.class.getCanonicalName(), Object.class.getCanonicalName()}), enhanceClazz);
        invokeMethod.setModifiers(Modifier.PUBLIC + Modifier.FINAL);

        // Cast type(强制类型转换)
        String invokeMethodBody = StringUtils.format("{this.bean.{}($1, ({})$2);}", method.getName(), packetClazz.getCanonicalName());
        invokeMethod.setBody(invokeMethodBody);

        enhanceClazz.addMethod(invokeMethod);

        enhanceClazz.detach();

        Class<IPacketReceiver> resultClazz = (Class<IPacketReceiver>) enhanceClazz.toClass();
        Constructor<?> resultConstructor = resultClazz.getConstructor(bean.getClass());
        IPacketReceiver receiver = (IPacketReceiver) resultConstructor.newInstance(bean);
        return receiver;
    }
}
