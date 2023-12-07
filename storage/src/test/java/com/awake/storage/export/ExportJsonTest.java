package com.awake.storage.export;

import com.awake.storage.util.ExportUtils;
import org.junit.Test;

/**
 * @version : 1.0
 * @ClassName: ExportJosnTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:36
 **/
public class ExportJsonTest {

    @Test
    public void test() throws Exception {
        var inputDir="G:\\ttdn\\justloseit\\storage\\src\\test\\resources\\excel";
        var outputDir ="G:\\ttdn\\justloseit\\storage\\src\\test\\resources\\excel";
        ExportUtils.excel2json(inputDir, outputDir);
    }

}
