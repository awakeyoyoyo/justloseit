package com.awakeyo.net.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @version : 1.0
 * @ClassName: NamespaceHandler
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/7/12 15:47
 **/
public class NamespaceHandler extends NamespaceHandlerSupport {

    private final String NET_CONFIG= "net-config";

    @Override
    public void init() {

        registerBeanDefinitionParser(NET_CONFIG, new NetConfigDefinitionParser());
    }

}

