package com.awake.storage.export;

import com.awake.storage.interpreter.CsvReader;
import com.awake.storage.util.ExportUtils;
import com.awake.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @version : 1.0
 * @ClassName: ExportCsvTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:22
 **/
public class ExportCsvTest {

    @Test
    public void exportTest() throws Exception {
        var inputDir="G:\\ttdn\\justloseit\\storage\\src\\test\\resources\\excel";
        var outputDir ="G:\\ttdn\\justloseit\\storage\\src\\test\\resources\\excel";
        ExportUtils.excel2csv(inputDir, outputDir);

        // godot4.x导入csv，莫名奇妙生成了一些translation文件，批量修改一下文件的名称
//        var csvFiles = ExportUtils.scanCsvFiles(outputDir);
//        csvFiles.stream().forEach(it -> it.renameTo(new File(StringUtils.format("{}.txt", it.getAbsolutePath()))));
//        csvFiles.stream().forEach(it -> FileUtils.deleteFile(it));
    }

    @Test
    public void csvReadTest() throws IOException {
        var file = new File("G:\\ttdn\\justloseit\\storage\\src\\test\\resources\\excel\\StudentCsvResource.csv");
        var list = CsvReader.readResourceDataFromCSV(FileUtils.openInputStream(file), file.getName());
        System.out.println(list);
    }
}
