package com.awake.storage.interpreter;

import com.awake.storage.interpreter.data.StorageData;
import com.awake.util.IOUtils;
import com.awake.util.JsonUtils;
import com.awake.util.base.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @version : 1.0
 * @ClassName: JsonReader
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:06
 **/
public class JsonReader {

    public static StorageData readResourceDataFromJson(InputStream input) {
        try {
            var resourceData= JsonUtils.string2Object(StringUtils.bytesToString(IOUtils.toByteArray(input)), StorageData.class);
            for(int i=0;i<resourceData.getHeaders().size();i++) {
                resourceData.getHeaders().get(i).setIndex(i);
            }
            return resourceData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
