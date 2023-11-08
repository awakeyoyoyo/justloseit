package com.awake.util;

import com.awake.util.base.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    /**
     * User's current working directory
     *
     * @return 绝对路径路径
     */
    public static String getProAbsPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 广度优先搜索文件
     *
     * @param fileOrDirectory 需要查找的文件夹
     * @return 所有可读的文件
     */
    public static List<File> getAllReadableFiles(File fileOrDirectory) {
        List<File> readableFileList = new ArrayList<>();
        Queue<File> queue = new LinkedList<>();
        queue.add(fileOrDirectory);
        while (!queue.isEmpty()) {
            var file = queue.poll();
            if (file.isDirectory()) {
                for (var f : file.listFiles()) {
                    queue.offer(f);
                }
                continue;
            }

            if (file.canRead()) {
                readableFileList.add(file);
            }
        }

        return readableFileList;
    }

    // ------------------------------------------------读取文件------------------------------------------------

    /**
     * Reads the contents of a file into a byte array.
     * The file is always closed.
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     * @throws IOException in case of an I/O error
     */
    public static byte[] readFileToByteArray(final File file) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.toByteArray(in); // Do NOT use file.length() - see IO-453
        } finally {
            IOUtils.closeIO(in);
        }
    }

    //---------------------------------打开，关闭，文件流--------------------------------------

    /**
     * Opens a {@link FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>.
     *
     * @param file the file to open for input, must not be {@code null}
     * @return a new {@link FileInputStream} for the specified file
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException           if the file object is a directory
     * @throws IOException           if the file cannot be read
     */
    public static FileInputStream openInputStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException(StringUtils.format("File [file:{}] exists but is a directory", file));
            }
            if (!file.canRead()) {
                throw new IOException(StringUtils.format("File [file:{}] cannot be read", file));
            }
        } else {
            throw new FileNotFoundException(StringUtils.format("File [file:{}] does not exist", file));
        }
        return new FileInputStream(file);
    }

    /**
     * Deletes a file. If file is a directory, delete it and all sub-directories.
     *
     * @param file file or directory to delete, must not be null
     */
    public static void deleteFile(final File file) {
        if (file.isDirectory()) {
            var files = file.listFiles();
            if (files != null) {
                for (var subFile : files) {
                    deleteFile(subFile);
                }
            }
            if (!file.delete()) {
                throw new RuntimeException("Unable to delete file directory: " + file);
            }
        } else {
            boolean filePresent = file.exists();
            if (filePresent) {
                if (!file.delete()) {
                    throw new RuntimeException("Unable to delete file: " + file);
                }
            }
        }
    }

}
