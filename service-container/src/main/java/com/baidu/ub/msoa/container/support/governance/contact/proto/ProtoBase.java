package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.squareup.protoparser.FieldElement;
import com.squareup.protoparser.MessageElement;
import com.squareup.protoparser.OptionElement;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.TypeElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/8/6.
 */
public class ProtoBase {

    protected boolean isCustomType(Class<?> fieldClazz) {
        return !fieldClazz.isPrimitive()
                && !fieldClazz.getName().startsWith("java")
                && !fieldClazz.getName().startsWith("javax")
                && !fieldClazz.getName().startsWith("sun")
                && !fieldClazz.getName().startsWith("com.sun");
    }

    protected String upperFirst(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    protected String getOptionValue(ProtoFile protoFile, String name) {
        OptionElement element = getOption(protoFile, name);
        return element != null ? (String) element.value() : null;
    }

    protected OptionElement getOption(ProtoFile protoFile, String name) {
        for (OptionElement optionElement : protoFile.options()) {
            if (optionElement.name().equals(name)) {
                return optionElement;
            }
        }
        return null;
    }

    protected List<String> getOptionValueByPrefix(ProtoFile protoFile, String prefix) {
        List<String> lines = new ArrayList<>();
        for (OptionElement element : getOptionByPrefix(protoFile, prefix)) {
            lines.add((String) element.value());
        }
        return lines;
    }

    protected List<OptionElement> getOptionByPrefix(ProtoFile protoFile, String prefix) {
        List<OptionElement> lines = new ArrayList<>();
        for (OptionElement optionElement : protoFile.options()) {
            if (optionElement.name().startsWith(prefix)) {
                lines.add(optionElement);
            }
        }
        return lines;
    }

    protected TypeElement getTypeElement(ProtoFile protoFile, String name) {

        for (TypeElement typeElement : protoFile.typeElements()) {
            if (typeElement.name().equals(name)) {
                return typeElement;
            }
        }

        return null;
    }

    protected FieldElement getFieldElement(MessageElement element, String name) {
        for (FieldElement fieldElement : element.fields()) {
            if (fieldElement.name().equals(name)) {
                return fieldElement;
            }
        }

        return null;
    }
}