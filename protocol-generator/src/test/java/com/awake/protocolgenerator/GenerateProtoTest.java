package com.awake.protocolgenerator;

import com.awake.protobuf.GeneratePbUtils;
import com.awake.protobuf.PbGenerateOperation;
import org.junit.Test;

/**
 * @version : 1.0
 * @ClassName: GenerateProtoTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/13 14:36
 **/
public class GenerateProtoTest {

    @Test
    public void generateClassFromProto() {
        var buildOption = new PbGenerateOperation();
        buildOption.setProtoPath("src\\test\\protobuf");
        buildOption.setOutputPath("src");

        buildOption.setJavaPackage("com.awake.pojo.test");
//        buildOption.setRecordClass(true);
//        buildOption.setOneProtocol(true);
        GeneratePbUtils.create(buildOption);
    }
}
