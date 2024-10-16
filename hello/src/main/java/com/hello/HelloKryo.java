package com.hello;

/**
 * @Author：lqh
 * @Date：2024/10/15 19:35
 */
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.*;
import java.util.Arrays;

public class HelloKryo {
    static public void main (String[] args) throws Exception {
        Kryo kryo = new Kryo();
        kryo.register(SomeClass.class);

        SomeClass object = new SomeClass();
        object.value = "Hello Kryo!";

        Output output = new Output(new ByteArrayOutputStream());
        kryo.writeObject(output, object);
        byte[] buffer = output.getBuffer();
        System.out.println(Arrays.toString(buffer));

        Input input = new Input(new ByteBufferInput(buffer));
        SomeClass object2 = kryo.readObject(input, SomeClass.class);
        System.out.println(object2.value);

    }
    static public class SomeClass {
        String value;
    }
}