
package org.mule.module.chatter.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.parsers.generic.AutoIdUtils;
import org.mule.module.chatter.config.ChatterModuleLifecycleAdapter;
import org.mule.util.TemplateParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ChatterModuleConfigDefinitionParser
    implements BeanDefinitionParser
{

    /**
     * Mule Pattern Info
     * 
     */
    private TemplateParser.PatternInfo patternInfo;

    public ChatterModuleConfigDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        String name = element.getAttribute("name");
        if ((name == null)||StringUtils.isBlank(name)) {
            element.setAttribute("name", AutoIdUtils.getUniqueName(element, "mule-bean"));
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ChatterModuleLifecycleAdapter.class.getName());
        if (Initialisable.class.isAssignableFrom(ChatterModuleLifecycleAdapter.class)) {
            builder.setInitMethodName(Initialisable.PHASE_NAME);
        }
        if (Disposable.class.isAssignableFrom(ChatterModuleLifecycleAdapter.class)) {
            builder.setDestroyMethodName(Disposable.PHASE_NAME);
        }
        if ((element.getAttribute("username")!= null)&&(!StringUtils.isBlank(element.getAttribute("username")))) {
            builder.addPropertyValue("username", element.getAttribute("username"));
        }
        if ((element.getAttribute("password")!= null)&&(!StringUtils.isBlank(element.getAttribute("password")))) {
            builder.addPropertyValue("password", element.getAttribute("password"));
        }
        if ((element.getAttribute("securityToken")!= null)&&(!StringUtils.isBlank(element.getAttribute("securityToken")))) {
            builder.addPropertyValue("securityToken", element.getAttribute("securityToken"));
        }
        if ((element.getAttribute("clientKey")!= null)&&(!StringUtils.isBlank(element.getAttribute("clientKey")))) {
            builder.addPropertyValue("clientKey", element.getAttribute("clientKey"));
        }
        if ((element.getAttribute("clientSecret")!= null)&&(!StringUtils.isBlank(element.getAttribute("clientSecret")))) {
            builder.addPropertyValue("clientSecret", element.getAttribute("clientSecret"));
        }
        if ((element.getAttribute("instanceUrl")!= null)&&(!StringUtils.isBlank(element.getAttribute("instanceUrl")))) {
            builder.addPropertyValue("instanceUrl", element.getAttribute("instanceUrl"));
        }
        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        return definition;
    }

}
