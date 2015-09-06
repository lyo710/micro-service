package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.baidu.ub.msoa.container.support.governance.contact.ContactConflictException;
import com.google.common.base.Verify;
import com.google.common.base.VerifyException;
import com.squareup.protoparser.FieldElement;
import com.squareup.protoparser.FieldElement.Label;
import com.squareup.protoparser.MessageElement;
import com.squareup.protoparser.TypeElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/8/10.
 */
public class TypeElementConflictChecker extends ProtoBase {

    public TypeElementConflictChecker(TypeElement source, TypeElement submit) {
        this.source = source;
        this.submit = submit;
    }

    private TypeElement source;
    private TypeElement submit;

    /**
     * 检查两个proto type是否冲突
     * 如果有冲突,抛出VerifyException
     */
    public void check() throws ContactConflictException {
        Verify.verify(submit != null, String.format("can not find exists type:[%s]", source.name()));
        Verify.verify(source.getClass() == submit.getClass(),
                String.format("invalid type of element:[%s], expect:[%s] but:[%s]",
                        source.name(),
                        source.getClass().getSimpleName(),
                        submit.getClass().getSimpleName()));

        if (source instanceof MessageElement) {
            checkMessage();
        } else {
            checkEnum();
        }
    }

    private void checkMessage() throws ContactConflictException {
        MessageElement sourceMessage = (MessageElement) source;
        MessageElement submitMessage = (MessageElement) submit;

        List<String> errors = new ArrayList<>();

        /* 检查已有的field是否改变 */
        for (FieldElement sourceField : sourceMessage.fields()) {
            FieldElement submitField = getFieldElement(submitMessage, sourceField.name());

            try {
                new FieldElementConflictChecker(sourceMessage, sourceField, submitField).check();
            } catch (VerifyException e) {
                errors.add(e.getMessage());
            } catch (Exception e) {
                errors.add(ExceptionUtils.getStackTrace(e));
            }
        }

        /* 检查新增的field是否为option */
        for (FieldElement submitField : submitMessage.fields()) {
            FieldElement sourceField = getFieldElement(sourceMessage, submitField.name());
            if (sourceField == null && submitField.label() != Label.OPTIONAL) {
                errors.add(String.format("the field:[%s#%s] must be option witch is first added",
                        sourceMessage.name(),
                        submitField.name()));
            }
        }

        if (!errors.isEmpty()) {
            throw new ContactConflictException(errors);
        }
    }

    private void checkEnum() {

    }

}
