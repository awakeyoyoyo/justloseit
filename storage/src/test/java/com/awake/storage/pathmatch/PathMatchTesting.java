package com.awake.storage.pathmatch;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * @version : 1.0
 * @ClassName: PathMatchTesing
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/12/7 16:17
 **/
public class PathMatchTesting {
    @Test
    public void resourceTest() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        Resource[] resources = resolver.getResources("classpath*:com/awake/storage/**/*.class");

        Resource[] resources = resolver.getResources("classpath:/excel/**/StudentResource.*");
        for (Resource res : resources) {
            System.out.println(res.getDescription());
        }
    }
}
