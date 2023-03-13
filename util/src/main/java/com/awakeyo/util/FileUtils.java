package com.awakeyo.util;

/**
 * @version : 1.0
 * @ClassName: FileUtils
 * @Description: 文件操作工具类
 * @Auther: awake
 * @Date: 2023/3/10 16:44
 **/
public class FileUtils {

    /**
     * 类Unix路径分隔符
     */
    private static final String UNIX_SEPARATOR = StringUtils.SLASH;
    /**
     * Windows路径分隔符
     */
    private static final String WINDOWS_SEPARATOR = StringUtils.BACK_SLASH;


    /**
     * 获取当前系统的换行分隔符
     * <pre>
     * Windows: \r\n
     * Mac: \r
     * Linux: \n
     * </pre>
     */
    public static final String LS = System.lineSeparator();
    public static final String UNIX_LS = "\\n";
    public static final String WINDOWS_LS = "\\r\\n";
    // The file copy buffer size (30 MB)
    private static final long FILE_COPY_BUFFER_SIZE = IOUtils.BYTES_PER_MB * 30;
}
