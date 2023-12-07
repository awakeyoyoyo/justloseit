package com.awake.storage.utils;

import com.awake.util.FileUtils;
import org.junit.Test;

/**
 * @version : 1.0
 * @ClassName: FileUTilsTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 17:29
 **/
public class FileUtilsTest {
    @Test
    public void test(){
        String ret = FileUtils.fileExtName("StudentCsvResource.class");
        System.out.println(ret);
    }
}
