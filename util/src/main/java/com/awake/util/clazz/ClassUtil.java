package com.awake.util.clazz;


import com.awake.exception.RunException;
import com.awake.util.AssertionUtils;
import com.awake.util.ReflectionUtils;
import com.awake.util.base.ArrayUtils;
import com.awake.util.base.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @version : 1.0
 * @ClassName: ScanUtil
 * @Description: 类工具
 * @Auther: awake
 * @Date: 2023/8/5 17:54
 **/
public class ClassUtil {

    private final static Log log = LogFactory.getLog(ClassUtil.class);
    /**
     *  扫描  scanPackages 下的文件的匹配符
     */
    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";


    /**
     * 结合spring的类扫描方式
     * 根据需要扫描的包路径及相应的注解，获取最终测method集合
     * 仅返回public方法，如果方法是非public类型的，不会被返回
     * 可以扫描工程下的class文件及jar中的class文件
     *
     * @param scanPackages
     * @param annotation
     * @return
     */
    public static Set<Method> scanClassAnnotationMethods(String scanPackages, Class<? extends Annotation> annotation) {
        //获取所有的类
        Set<Class> clazzSet = scanPackageClass(scanPackages);
        Set<Method> methods = new HashSet<Method>();
        //遍历类，查询相应的annotation方法
        for (Class clazz : clazzSet) {
            try {
                Set<Method> ms = scanAnnotationMethods(clazz, annotation);
                if (ms != null) {
                    methods.addAll(ms);
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
        return methods;
    }

    /**
     * 从类路径中读取文件
     *
     * @param filePath 一般指resources中的文件，也可以在jar中
     */
    public static InputStream getFileFromClassPath(String filePath) throws IOException {
        var classLoader = getDefaultClassLoader();
        var resource = classLoader.getResource(filePath);
        return resource.openStream();
    }

    public static ClassLoader getDefaultClassLoader() {
        var classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            return classLoader;
        }

        // No thread context class loader -> use class loader of this class.
        classLoader = ClassUtil.class.getClassLoader();
        if (classLoader != null) {
            return classLoader;
        }

        // getClassLoader() returning null indicates the bootstrap ClassLoader
        classLoader = ClassLoader.getSystemClassLoader();
        return classLoader;
    }


    /**
     * 根据扫描包的,查询下面的所有类   注解排查
     *
     * @param scanPackages 扫描的package路径
     * @return
     */
    public static Set<Class> scanPackageClass(String scanPackages,String annoName) {
        if (StringUtils.isBlank(scanPackages)) {
            return Collections.EMPTY_SET;
        }
        //验证及排重包路径,避免父子路径多次扫描
        Set<String> packages = checkPackage(scanPackages);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        Set<Class> result = new HashSet();
        for (String basePackage : packages) {
            if (StringUtils.isBlank(basePackage)) {
                continue;
            }
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    org.springframework.util.ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/" + DEFAULT_RESOURCE_PATTERN;
            try {
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    //检查resource，这里的resource都是class
                    if (resource.isReadable()) {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        AnnotationMetadata annoMeta = metadataReader.getAnnotationMetadata();
                        if (StringUtils.isEmpty(annoName)) {
                            ClassMetadata clazzMeta = metadataReader.getClassMetadata();
                            result.add(Class.forName(clazzMeta.getClassName()));
                        } else if (annoMeta.hasAnnotation(annoName)) {
                            ClassMetadata clazzMeta = metadataReader.getClassMetadata();
                            result.add(Class.forName(clazzMeta.getClassName()));
                        }
                    }
                }
            } catch (Exception e) {
                log.error("获取包下面的类信息失败,package:" + basePackage, e);
            }

        }
        return result;
    }

    /**
     * 根据扫描包的,查询下面的所有类
     *
     * @param scanPackages 扫描的package路径
     * @return
     */
    public static Set<Class> scanPackageClass(String scanPackages){
       return scanPackageClass(scanPackages,null);
    }

    /**
     * 排重、检测package父子关系，避免多次扫描
     *
     * @param scanPackages
     * @return 返回检查后有效的路径集合
     */
    private static Set<String> checkPackage(String scanPackages) {
        if (StringUtils.isBlank(scanPackages)) {
            return Collections.EMPTY_SET;
        }
        Set<String> packages = new HashSet<String>();
        //排重路径
        Collections.addAll(packages, scanPackages.split(","));
        for (String pInArr : packages.toArray(new String[packages.size()])) {
            if (StringUtils.isBlank(pInArr) || pInArr.equals(".") || pInArr.startsWith(".")) {
                continue;
            }
            if (pInArr.endsWith(".")) {
                pInArr = pInArr.substring(0, pInArr.length() - 1);
            }
            Iterator<String> packageIte = packages.iterator();
            boolean needAdd = true;
            while (packageIte.hasNext()) {
                String pack = packageIte.next();
                if (pInArr.startsWith(pack + ".")) {
                    //如果待加入的路径是已经加入的pack的子集，不加入
                    needAdd = false;
                } else if (pack.startsWith(pInArr + ".")) {
                    //如果待加入的路径是已经加入的pack的父集，删除已加入的pack
                    packageIte.remove();
                }
            }
            if (needAdd) {
                packages.add(pInArr);
            }
        }
        return packages;
    }


    /**
     * 加载资源，根据resource获取className
     *
     * @param metadataReaderFactory spring中用来读取resource为class的工具
     * @param resource              这里的资源就是一个Class
     * @throws IOException
     */
    private static String loadClassName(MetadataReaderFactory metadataReaderFactory, Resource resource) throws IOException {
        try {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (metadataReader != null) {
                    return metadataReader.getClassMetadata().getClassName();
                }
            }
        } catch (Exception e) {
            log.error("根据resource获取类名称失败", e);
        }
        return null;
    }

    /**
     * 把action下面的所有method遍历一次，标记他们是否需要进行敏感词验证
     * 如果需要，放入cache中
     *
     */
    public static Set<Method> scanAnnotationMethods(Class<?> clz , Class<? extends Annotation> anno) throws ClassNotFoundException {
        Set<Method> methodSet = new HashSet<Method>();
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getModifiers() != Modifier.PUBLIC) {
                continue;
            }
            Annotation annotation = method.getAnnotation(anno);
            if (annotation != null) {
                methodSet.add(method);
            }
        }
        return methodSet;
    }

    public static void main(String[] args) {
        String packages = "com.awake.exception,com.awake.util";
       System.out.println(scanPackageClass(packages));
    }
    public static Set<Class<?>> relevantClass(Class<?> clazz) {
        var classSet = new HashSet<Class<?>>();
        return relevantClass(clazz, classSet);
    }


    private static Set<Class<?>> relevantClass(Class<?> clazz, Set<Class<?>> classSet) {
        if (!classSet.add(clazz)) {
            return classSet;
        }

        // 是否为一个简单的javabean，为了防止不同层对象混用造成潜在的并发问题，特别是网络层和po层混用
        // 不能是泛型类
        AssertionUtils.isTrue(ArrayUtils.isEmpty(clazz.getTypeParameters()), "[class:{}] cannot be generic class", clazz.getCanonicalName());

        var filedList = ReflectionUtils.notStaticAndTransientFields(clazz);

        for (var field : filedList) {
            // 是一个基本类型变量
            var fieldType = field.getType();
            if (isBaseType(fieldType)) {
                // do nothing
            } else if (fieldType.isArray()) {
                // 是一个数组
                Class<?> arrayClazz = fieldType.getComponentType();
                relevantClass0(arrayClazz, classSet);
            } else if (Set.class.isAssignableFrom(fieldType)) {
                AssertionUtils.isTrue(fieldType.equals(Set.class), "[class:{}]类型声明不正确，必须是Set接口类型", clazz.getCanonicalName());

                var type = field.getGenericType();
                AssertionUtils.isTrue(type instanceof ParameterizedType, "[class:{}]类型声明不正确，不是泛型类[field:{}]", clazz.getCanonicalName(), field.getName());

                var types = ((ParameterizedType) type).getActualTypeArguments();
                AssertionUtils.isTrue(types.length == 1, "[class:{}]中Set类型声明不正确，[field:{}]必须声明泛型类", clazz.getCanonicalName(), field.getName());

                relevantClass0(types[0], classSet);
            } else if (List.class.isAssignableFrom(fieldType)) {
                // 是一个List
                AssertionUtils.isTrue(fieldType.equals(List.class), "[class:{}]类型声明不正确，必须是List接口类型", clazz.getCanonicalName());

                var type = field.getGenericType();
                AssertionUtils.isTrue(type instanceof ParameterizedType, "[class:{}]类型声明不正确，不是泛型类[field:{}]", clazz.getCanonicalName(), field.getName());

                var types = ((ParameterizedType) type).getActualTypeArguments();
                AssertionUtils.isTrue(types.length == 1, "[class:{}]中List类型声明不正确，[field:{}]必须声明泛型类", clazz.getCanonicalName(), field.getName());

                relevantClass0(types[0], classSet);
            } else if (Map.class.isAssignableFrom(fieldType)) {
                if (!fieldType.equals(Map.class)) {
                    throw new RunException("[class:{}]类型声明不正确，必须是Map接口类型", clazz.getCanonicalName());
                }

                var type = field.getGenericType();
                if (!(type instanceof ParameterizedType)) {
                    throw new RunException("[class:{}]中数组类型声明不正确，[field:{}]不是泛型类", clazz.getCanonicalName(), field.getName());
                }

                var types = ((ParameterizedType) type).getActualTypeArguments();
                if (types.length != 2) {
                    throw new RunException("[class:{}]中数组类型声明不正确，[field:{}]必须声明泛型类", clazz.getCanonicalName(), field.getName());
                }

                var keyType = types[0];
                var valueType = types[1];
                relevantClass0(keyType, classSet);
                relevantClass0(valueType, classSet);
            } else {
                relevantClass(fieldType, classSet);
            }
        }

        return classSet;
    }

    private static void relevantClass0(Type type, Set<Class<?>> classSet) {
        if (type instanceof ParameterizedType) {
            // 泛型类
            Class<?> clazz = (Class<?>) ((ParameterizedType) type).getRawType();
            if (Set.class.equals(clazz)) {
                // Set<Set<String>>
                relevantClass0(((ParameterizedType) type).getActualTypeArguments()[0], classSet);
                return;
            } else if (List.class.equals(clazz)) {
                // List<List<String>>
                relevantClass0(((ParameterizedType) type).getActualTypeArguments()[0], classSet);
                return;
            } else if (Map.class.equals(clazz)) {
                // Map<List<String>, List<String>>
                var types = ((ParameterizedType) type).getActualTypeArguments();
                var keyType = types[0];
                var valueType = types[1];
                relevantClass0(keyType, classSet);
                relevantClass0(valueType, classSet);
                return;
            }
        } else if (type instanceof Class) {
            Class<?> clazz = ((Class<?>) type);
            if (isBaseType(clazz)) {
                // do nothing
                return;
            } else if (clazz.getComponentType() != null) {
                // 是一个二维以上数组
                throw new RunException("不支持多维数组或集合嵌套数组[type:{}]类型，仅支持一维数组", type);
            } else if (clazz.equals(List.class) || clazz.equals(Set.class) || clazz.equals(Map.class)) {
                throw new RunException("不支持数组和集合联合使用[type:{}]类型", type);
            } else {
                if (!classSet.add(clazz)) {
                    return;
                }
                relevantClass(clazz, classSet);
                return;
            }
        }
        throw new RunException("[type:{}]类型不正确", type);
    }

    private static boolean isBaseType(Class<?> clazz) {
        return clazz.isPrimitive()
                || Number.class.isAssignableFrom(clazz)
                || String.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz);
    }
}
