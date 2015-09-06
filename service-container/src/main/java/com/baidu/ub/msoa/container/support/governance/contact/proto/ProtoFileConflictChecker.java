package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.baidu.ub.msoa.container.support.governance.contact.ContactConflictException;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.TypeElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/8/10.
 */
public class ProtoFileConflictChecker extends ProtoBase {

    public ProtoFileConflictChecker(ProtoFile source, ProtoFile submit) {
        this.source = source;
        this.submit = submit;
    }

    private ProtoFile source;
    private ProtoFile submit;

    private Class<?> sourceStubClazz;
    private Class<?> submitStubClazz;

    /**
     * 检查两个proto file是否冲突
     * 如果有冲突,抛出ProtoConflictException
     */
    public void check() throws ContactConflictException {

        /* 检查接口签名是否改变 */
        // checkService();

        /* 检查类型是否改变 */
        checkType();
    }

    private void checkService() throws ContactConflictException {
        List<String> errors = new ArrayList<>();

        try {
            checkInterface(errors);

            //            for (Method sourceMethod : sourceStubClazz.getDeclaredMethods()) {
            //                Method submitMethod = null;
            //
            //                try {
            //                    submitMethod = submitStubClazz.getMethod(sourceMethod.getName(), sourceMethod.getParameterTypes());
            //
            //                    if (Arrays.hashCode(sourceMethod.getParameterTypes())
            //                        != Arrays.hashCode(submitMethod.getParameterTypes())) {
            //                        errors.add(String.format("expect method:[%s],but submit is:[%s]",
            //                                                 sourceMethod.toGenericString(),
            //                                                 submitMethod.toGenericString()));
            //                    }
            //                } catch (NoSuchMethodException e) {
            //                    errors.add(String.format("can not find method:[%s] in submit contact",
            //                                             sourceMethod.getAnnotation(ComponentInvoker.class)));
            //                }
            //            }

        } catch (ContactConflictException e) {
            errors.addAll(e.getErrors());
        } catch (Exception e) {
            errors.add(ExceptionUtils.getStackTrace(e));
        }

        if (!errors.isEmpty()) {
            throw new ContactConflictException(errors);
        }
    }

    private void checkInterface(List<String> errors) throws Exception {
        ServiceStubClassGenerator sourceGenerator = new ServiceStubClassGenerator(source.toSchema());
        sourceGenerator.generate();
        sourceStubClazz = sourceGenerator.getServiceStubClazz();

        ServiceStubClassGenerator submitGenerator = new ServiceStubClassGenerator(submit.toSchema());
        submitGenerator.generate();
        submitStubClazz = sourceGenerator.getServiceStubClazz();

        if (sourceStubClazz != submitStubClazz) {
            errors.add(String.format("expect service is:[%s] but submit is:[%s]", sourceStubClazz, submitStubClazz));
        }
    }

    private void checkType() throws ContactConflictException {
        List<String> errors = new ArrayList<>();
        for (TypeElement sourceElement : source.typeElements()) {
            TypeElement submitElement = getTypeElement(submit, sourceElement.name());
            try {
                new TypeElementConflictChecker(sourceElement, submitElement).check();
            } catch (ContactConflictException e) {
                errors.addAll(e.getErrors());
            } catch (Exception e) {
                errors.add(ExceptionUtils.getStackTrace(e));
            }
        }

        if (!errors.isEmpty()) {
            throw new ContactConflictException(errors);
        }
    }
}
