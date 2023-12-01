package com.awake.storage.interpreter;

import com.awake.exception.RunException;
import com.awake.storage.interpreter.data.StorageData;
import com.awake.storage.interpreter.data.StorageHeader;
import com.awake.storage.util.CellUtils;
import com.awake.util.base.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @version : 1.0
 * @ClassName: ExcelReader
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:06
 **/
public class ExcelReader {

    public static StorageData readResourceDataFromExcel(InputStream inputStream, String resourceClassName) {
        // 只读取代码里写的字段
        var wb = createWorkbook(inputStream, resourceClassName);
        // 默认取到第一个sheet页
        var sheet = wb.getSheetAt(0);
        var iterator = sheet.iterator();
        // 设置所有列
        var excelHeaders = getHeaders(iterator, resourceClassName);

        var rows = new ArrayList<List<String>>();
        while (iterator.hasNext()) {
            var row = iterator.next();

            var idCell = row.getCell(0);
            if (StringUtils.isBlank(CellUtils.getCellStringValue(idCell))) {
                continue;
            }

            var columns = new ArrayList<String>();
            for (var header : excelHeaders) {
                var cell = row.getCell(header.getIndex());
                var content = CellUtils.getCellStringValue(cell);
                columns.add(content);
            }
            rows.add(columns);
        }

        // excel某些格子空的引起的列偏移
        var headers = new ArrayList<StorageHeader>();
        for (var i = 0; i < excelHeaders.size(); i++) {
            var excelHeader = excelHeaders.get(i);
            headers.add(StorageHeader.valueOf(excelHeader.getName(), excelHeader.getType(), i));
        }
        return StorageData.valueOf(resourceClassName, headers, rows);
    }

    private static List<StorageHeader> getHeaders(Iterator<Row> iterator, String resourceClassName) {
        // 获取配置表的有效列名称，默认第一行就是字段名称
        var fieldRow = iterator.next();
        if (fieldRow == null) {
            throw new RunException("Failed to get attribute control column from excel file of resource [class:{}]", resourceClassName);
        }
        // 默认第二行字段类型
        var typeRow = iterator.next();
        if (typeRow == null) {
            throw new RunException("Failed to get type control column from excel file of resource [class:{}]", resourceClassName);
        }
        // 默认第三行为描述，需要的时候再使用
        var desRow = iterator.next();
        var headerList = new ArrayList<StorageHeader>();
        var cellFieldMap = new HashMap<String, Integer>();
        for (var i = 0; i < fieldRow.getLastCellNum(); i++) {
            var fieldCell = fieldRow.getCell(i);
            if (Objects.isNull(fieldCell)) {
                continue;
            }
            var typeCell = typeRow.getCell(i);
            if (Objects.isNull(typeCell)) {
                continue;
            }
            var excelFieldName = CellUtils.getCellStringValue(fieldCell);
            if (StringUtils.isEmpty(excelFieldName)) {
                continue;
            }
            var typeName = CellUtils.getCellStringValue(typeCell);
            if (StringUtils.isEmpty(typeName)) {
                continue;
            }
            var previousValue = cellFieldMap.put(excelFieldName, i);
            if (Objects.nonNull(previousValue)) {
                throw new RunException("There are duplicate attribute control columns [field:{}] in the Excel file of the resource [class:{}]", excelFieldName,resourceClassName);
            }
            headerList.add(StorageHeader.valueOf(excelFieldName, typeName, i));
        }
        return headerList;
    }

    private static Workbook createWorkbook(InputStream input, String fileName) {
        try {
            return WorkbookFactory.create(input);
        } catch (IOException e) {
            throw new RunException("Static excel resource [{}] is abnormal, and the file cannot be read", fileName, e);
        }
    }
}
