package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.baidu.ub.msoa.container.support.governance.contact.proto.annotaion.Optional;
import com.baidu.ub.msoa.container.support.governance.contact.proto.annotaion.Tag;
import com.baidu.ub.msoa.utils.JavassistUtil;
import com.squareup.protoparser.EnumConstantElement;
import com.squareup.protoparser.EnumElement;
import com.squareup.protoparser.FieldElement;
import com.squareup.protoparser.FieldElement.Label;
import com.squareup.protoparser.MessageElement;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.ProtoParser;
import com.squareup.protoparser.TypeElement;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Loader;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.IntegerMemberValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pippo on 15/7/26.
 */
public class ProtoClassGenerator extends ProtoBase {

    private static Logger logger = LoggerFactory.getLogger(ProtoClassGenerator.class);

    public ProtoClassGenerator(String schema) {
        this(schema, false);
    }

    public ProtoClassGenerator(String schema, boolean writeClazz) {
        this.protoFile = ProtoParser.parse("", schema);
        this.writeClazz = writeClazz;
        this.timestamp = System.currentTimeMillis();
        this.pool = JavassistUtil.createPool();

        Loader loader = JavassistUtil.createLoader(pool);
        loader.delegateLoadingOf("com.squareup.protoparser.");
        loader.delegateLoadingOf(Optional.class.getName());
        loader.delegateLoadingOf(Tag.class.getName());
        this.loader = loader;

        Thread.currentThread().setContextClassLoader(this.loader);
    }

    protected boolean writeClazz = false;
    protected long timestamp;
    protected ClassPool pool;
    protected ClassLoader loader;
    protected ProtoFile protoFile;
    protected Set<Class<?>> generatedClazz = new HashSet<>();

    public void setWriteClazz(boolean writeClazz) {
        this.writeClazz = writeClazz;
    }

    public Set<Class<?>> generate() throws Exception {
        for (TypeElement element : protoFile.typeElements()) {
            createClazz(element);
        }

        if (writeClazz) {
            CtClass ctClass;
            for (Class<?> clazz : generatedClazz) {
                ctClass = pool.get(clazz.getName());
                ctClass.writeFile("generated/" + timestamp);
                ctClass.defrost();
            }
        }

        return generatedClazz;
    }

    protected void createClazz(TypeElement element) throws Exception {
        Class<?> clazz;

        if (element instanceof MessageElement) {
            clazz = createClazz((MessageElement) element);
        } else {
            clazz = createEnum((EnumElement) element);
        }

        logger.debug("created pojo class:[{}]", clazz);

        loadClass(clazz.getName());
        generatedClazz.add(clazz);
    }

    protected Class<?> createClazz(MessageElement element) throws Exception {
        CtClass ctClass = pool.getOrNull(element.name());
        if (ctClass != null) {
            ctClass.defrost();
        }

        ctClass = pool.makeClass(element.name());

        for (FieldElement fieldElement : element.fields()) {
            createPOJOField(ctClass, fieldElement);
        }

        Class<?> clazz = ctClass.toClass();

        //        for (Field field : clazz.getDeclaredFields()) {
        //            if (!isCustomType(field.getType()) || Modifier.isStatic(field.getModifiers())) {
        //                continue;
        //            }
        //            generatedClazz.add(loadClass(field.getType().getName()));
        //        }

        return clazz;
    }

    protected Class<?> createEnum(EnumElement element) throws Exception {
        CtClass ctClass = pool.getOrNull(element.name());
        if (ctClass != null) {
            ctClass.defrost();
        }

        ctClass = pool.makeClass(element.name());
        ctClass.setModifiers(Modifier.PUBLIC + Modifier.FINAL + Modifier.ENUM);
        ctClass.setSuperclass(pool.get(Enum.class.getName()));

        CtConstructor constructor =
                new CtConstructor(new CtClass[] { pool.get(String.class.getName()), pool.get("int") }, ctClass);
        constructor.setModifiers(Modifier.PROTECTED);
        constructor.setBody("super($$);");
        ctClass.addConstructor(constructor);

        int i = 0;
        final StringBuilder sb = new StringBuilder();
        for (EnumConstantElement constant : element.constants()) {
            CtField item = new CtField(ctClass, constant.name(), ctClass);
            item.setModifiers(Modifier.PUBLIC + Modifier.STATIC + Modifier.FINAL + Modifier.ENUM);
            ctClass.addField(item, String.format("new %s(\"%s\",%s)", ctClass.getName(), constant.name(), i++));
            sb.append(constant.name()).append(",");
        }

        ctClass.addField(CtField.make(String.format("private static %s[] $VALUES=new %s[]{%s};",
                ctClass.getName(),
                ctClass.getName(),
                sb.substring(0, sb.length() - 1)), ctClass));
        ctClass.addMethod(CtMethod.make(String.format("public static %s[] values(){ return $VALUES; }",
                ctClass.getName()), ctClass));

        // TODO enum中其他的static方法也需要添加
        return ctClass.toClass();
    }

    protected void createPOJOField(CtClass ctClass, FieldElement fieldElement) throws Exception {
        Class<?> fieldType = ProtoType.getPrimitiveType(fieldElement.type());

        if (fieldType == null) {
            fieldType = loadClass(fieldElement.type().toString());
        }

        String src = String.format("private %s %s;",
                fieldElement.label() == Label.REPEATED ? fieldType.getName() + "[]" : fieldType.getName(),
                fieldElement.name());

        CtField ctField = CtField.make(src, ctClass);
        addFieldAnnotation(fieldType, ctField, fieldElement);

        ctClass.addField(ctField);
        ctClass.addMethod(CtNewMethod.getter("get" + upperFirst(fieldElement.name()), ctField));
        ctClass.addMethod(CtNewMethod.setter("set" + upperFirst(fieldElement.name()), ctField));
    }

    protected void addFieldAnnotation(Class<?> fieldType, CtField ctField, FieldElement fieldElement)
            throws NotFoundException {
        ConstPool constPool = ctField.getFieldInfo().getConstPool();

        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);

        Annotation tag = new Annotation(Tag.class.getName(), constPool);
        tag.addMemberValue("value", new IntegerMemberValue(constPool, fieldElement.tag()));
        attr.addAnnotation(tag);

        if (fieldElement.label() == Label.OPTIONAL) {
            attr.addAnnotation(new Annotation(Optional.class.getName(), constPool));
        }

        ctField.getFieldInfo().addAttribute(attr);
    }

    protected Class<?> loadClass(String name) {
        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
