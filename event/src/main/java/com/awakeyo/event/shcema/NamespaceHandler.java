package com.awakeyo.event.shcema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @version : 1.0
 * @ClassName: NamespaceHandler
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/3/3 17:25
 **/
public class NamespaceHandler extends NamespaceHandlerSupport {

    public static final String EVENT = "event";

    @Override
    public void init() {
        registerBeanDefinitionParser(EVENT, new EventDefinitionParser());
    }
}
