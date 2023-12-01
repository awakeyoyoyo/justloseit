package com.awake.storage.strategy;

import com.awake.util.base.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version : 1.0
 * @ClassName: StringToDataConverter
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/1 11:15
 **/
public class StringToDateConverter implements Converter<String, Date> {


    @Override
    public Date convert(String source) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return df.parse(source);
        } catch (ParseException e) {
            throw new IllegalArgumentException(StringUtils.format("The string [{}] does not meet the format requirements: [yyyy-MM-dd HH:mm:ss]", source));
        }
    }
}
