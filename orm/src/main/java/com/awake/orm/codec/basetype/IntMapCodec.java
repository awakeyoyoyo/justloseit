package com.awake.orm.codec.basetype;


import com.awake.orm.codec.MapKeyCodec;

/**
 * map解析器
 *
 * @Author：lqh
 * @Date：2024/6/14 10:32
 */
public class IntMapCodec implements MapKeyCodec<Integer> {


    @Override
    public String encode(Integer value) {
        return value.toString();
    }

    @Override
    public Integer decode(String text) {
        return (text == null) ? null : Integer.parseInt(text);
    }
}
