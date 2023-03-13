package com.awakeyo.event.shcema;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import javax.naming.event.EventContext;

/**
 * @version : 1.0
 * @ClassName: EventDefinitionParser
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/3/3 17:27
 **/
public class EventDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        Class<?> clazz;
        String name;
        BeanDefinitionBuilder builder;

//         注册EventContext
        clazz = EventContext.class;
        name = StringUtils.uncapitalize(clazz.getName());
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(name, builder.getBeanDefinition());

        // 注册EventRegisterProcessor，event事件处理
        clazz = EventRegisterProcessor.class;
        name = StringUtils.uncapitalize(clazz.getName());
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(name, builder.getBeanDefinition());

        return builder.getBeanDefinition();
    }
}
