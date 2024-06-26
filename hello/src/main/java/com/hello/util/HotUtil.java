package com.hello.util;

import com.awake.storage.StorageContext;
import com.awake.storage.manager.AbstractStorage;
import com.awake.util.FileUtils;
import com.awake.util.HotSwapUtils;
import com.awake.util.IOUtils;
import com.awake.util.base.StringUtils;
import com.awake.util.clazz.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 热更配置和类 工具类
 * @Author：lqh
 * @Date：2024/5/21 11:31
 */
public class HotUtil {

    private static final Logger logger = LoggerFactory.getLogger(HotUtil.class);

    public static Map<String, Class<?>> configSimpleClazzNameMap() {
        Map<String, Class<?>> clazzSimpleNameMap = StorageContext.getStorageManager()
                .storageMap()
                .keySet()
                .stream()
                .collect(Collectors.toMap(key -> key.getSimpleName(), value -> value));
        return clazzSimpleNameMap;
    }

    /**
     * 热更配置文件
     *
     * @param excelNameList role,common
     * @throws IOException
     */
    public static void startHotSwapConfig(String excelNameList) throws IOException {
        String[] hotExcel = excelNameList.split(StringUtils.COMMA);
        Map<String, Class<?>> clazzSimpleNameMap = configSimpleClazzNameMap();

        List<String> hotswapExcelList = new ArrayList<>();
        for (String excelName : hotExcel) {
            var clazz = clazzSimpleNameMap.get(excelName);
            // 如果该excel在当前项目中没有被使用，则不必解析配置表，也不用去更新了
            var currentStorage = StorageContext.getStorageManager().getStorage(clazz);
            if (currentStorage == null || currentStorage.isRecycle()) {
                continue;
            }
            Resource resource = StorageContext.getStorageManager().resource(clazz);
            var fileExtName = FileUtils.fileExtName(resource.getFilename());
            AbstractStorage<?, ?> storageObject = AbstractStorage.parse(resource.getInputStream(), clazz, fileExtName);
            StorageContext.getStorageManager().updateStorage(clazz, storageObject);
            hotswapExcelList.add(excelName);
        }
        if (!hotswapExcelList.isEmpty()) {
            StorageContext.getStorageManager().inject();
        }
        logger.info("[HotUtil] hotswap excel success ! update excel:{}", hotswapExcelList);
    }

    /**
     * 热更class
     *
     * @param classPaths
     * @throws IOException
     */
    public static void startHotSwapClass(List<String> classPaths) throws IOException {
        for (String classPath : classPaths) {
            logger.info("[HotUtil] hotswap class:{} begin", classPath);
            InputStream inputStream = ClassUtil.getFileFromClassPath(classPath);
            byte[] byteArray = IOUtils.toByteArray(inputStream);
            HotSwapUtils.hotswapClass(byteArray);
            logger.info("[HotUtil] hotswap class:{} end", classPath);
        }
    }

}
