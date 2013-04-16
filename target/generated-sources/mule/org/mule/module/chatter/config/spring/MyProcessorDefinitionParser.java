
package org.mule.module.chatter.config.spring;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.util.SpringXMLUtils;
import org.mule.module.chatter.config.MyProcessorMessageProcessor;
import org.mule.util.TemplateParser;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class MyProcessorDefinitionParser
    implements BeanDefinitionParser
{

    /**
     * Mule Pattern Info
     * 
     */
    private TemplateParser.PatternInfo patternInfo;

    public MyProcessorDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(MyProcessorMessageProcessor.class.getName());
        String configRef = element.getAttribute("config-ref");
        if ((configRef!= null)&&(!StringUtils.isBlank(configRef))) {
            builder.addPropertyValue("moduleObject", configRef);
        }
        Element messageSegmentsListElement = null;
        messageSegmentsListElement = DomUtils.getChildElementByTagName(element, "message-segments");
        List<Element> messageSegmentsListChilds = null;
        if (messageSegmentsListElement!= null) {
            String messageSegmentsRef = messageSegmentsListElement.getAttribute("ref");
            if ((messageSegmentsRef!= null)&&(!StringUtils.isBlank(messageSegmentsRef))) {
                if ((!messageSegmentsRef.startsWith(patternInfo.getPrefix()))&&(!messageSegmentsRef.endsWith(patternInfo.getSuffix()))) {
                    builder.addPropertyValue("messageSegments", new RuntimeBeanReference(messageSegmentsRef));
                } else {
                    builder.addPropertyValue("messageSegments", messageSegmentsRef);
                }
            } else {
                ManagedList messageSegments = new ManagedList();
                messageSegmentsListChilds = DomUtils.getChildElementsByTagName(messageSegmentsListElement, "message-segment");
                if (messageSegmentsListChilds!= null) {
                    for (Element messageSegmentsChild: messageSegmentsListChilds) {
                        String valueRef = messageSegmentsChild.getAttribute("value-ref");
                        if ((valueRef!= null)&&(!StringUtils.isBlank(valueRef))) {
                            messageSegments.add(new RuntimeBeanReference(valueRef));
                        } else {
                            List<Element> innermessagesegmentListChilds = null;
                            if (messageSegmentsChild!= null) {
                                String innermessagesegmentRef = messageSegmentsChild.getAttribute("ref");
                                if ((innermessagesegmentRef!= null)&&(!StringUtils.isBlank(innermessagesegmentRef))) {
                                    if ((!innermessagesegmentRef.startsWith(patternInfo.getPrefix()))&&(!innermessagesegmentRef.endsWith(patternInfo.getSuffix()))) {
                                        builder.addPropertyValue("inner-message-segment", new RuntimeBeanReference(innermessagesegmentRef));
                                    } else {
                                        builder.addPropertyValue("inner-message-segment", innermessagesegmentRef);
                                    }
                                } else {
                                    ManagedMap innermessagesegment = new ManagedMap();
                                    innermessagesegmentListChilds = DomUtils.getChildElementsByTagName(messageSegmentsChild, "inner-message-segment");
                                    if (innermessagesegmentListChilds!= null) {
                                        if (innermessagesegmentListChilds.size() == 0) {
                                            innermessagesegmentListChilds = DomUtils.getChildElements(messageSegmentsChild);
                                        }
                                        for (Element innermessagesegmentChild: innermessagesegmentListChilds) {
                                            String innermessagesegmentValueRef = innermessagesegmentChild.getAttribute("value-ref");
                                            String innermessagesegmentKeyRef = innermessagesegmentChild.getAttribute("key-ref");
                                            Object valueObject = null;
                                            Object keyObject = null;
                                            if ((innermessagesegmentValueRef!= null)&&(!StringUtils.isBlank(innermessagesegmentValueRef))) {
                                                valueObject = new RuntimeBeanReference(innermessagesegmentValueRef);
                                            } else {
                                                valueObject = innermessagesegmentChild.getTextContent();
                                            }
                                            if ((innermessagesegmentKeyRef!= null)&&(!StringUtils.isBlank(innermessagesegmentKeyRef))) {
                                                keyObject = new RuntimeBeanReference(innermessagesegmentKeyRef);
                                            } else {
                                                keyObject = innermessagesegmentChild.getAttribute("key");
                                            }
                                            if ((keyObject == null)||((keyObject instanceof String)&&StringUtils.isBlank(((String) keyObject)))) {
                                                keyObject = innermessagesegmentChild.getTagName();
                                            }
                                            innermessagesegment.put(keyObject, valueObject);
                                        }
                                    }
                                    messageSegments.add(innermessagesegment);
                                }
                            }
                        }
                    }
                }
                builder.addPropertyValue("messageSegments", messageSegments);
            }
        }
        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        MutablePropertyValues propertyValues = parserContent.getContainingBeanDefinition().getPropertyValues();
        if (parserContent.getContainingBeanDefinition().getBeanClassName().equals("org.mule.config.spring.factories.PollingMessageSourceFactoryBean")) {
            propertyValues.addPropertyValue("messageProcessor", definition);
        } else {
            PropertyValue messageProcessors = propertyValues.getPropertyValue("messageProcessors");
            if ((messageProcessors == null)||(messageProcessors.getValue() == null)) {
                propertyValues.addPropertyValue("messageProcessors", new ManagedList());
            }
            List listMessageProcessors = ((List) propertyValues.getPropertyValue("messageProcessors").getValue());
            listMessageProcessors.add(definition);
        }
        return definition;
    }

    protected String getAttributeValue(Element element, String attributeName) {
        if (!StringUtils.isEmpty(element.getAttribute(attributeName))) {
            return element.getAttribute(attributeName);
        }
        return null;
    }

    private String generateChildBeanName(Element element) {
        String id = SpringXMLUtils.getNameOrId(element);
        if (StringUtils.isBlank(id)) {
            String parentId = SpringXMLUtils.getNameOrId(((Element) element.getParentNode()));
            return ((("."+ parentId)+":")+ element.getLocalName());
        } else {
            return id;
        }
    }

}
