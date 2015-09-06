package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.google.common.base.Verify;
import com.squareup.protoparser.FieldElement;
import com.squareup.protoparser.FieldElement.Label;
import com.squareup.protoparser.TypeElement;

/**
 * Created by pippo on 15/8/10.
 */
public class FieldElementConflictChecker {

    public FieldElementConflictChecker(TypeElement parent, FieldElement source, FieldElement submit) {
        this.parent = parent;
        this.source = source;
        this.submit = submit;
    }

    private TypeElement parent;
    private FieldElement source;
    private FieldElement submit;

    /**
     * 检查两个proto field是否冲突
     * 如果有冲突,抛出VerifyException
     */
    public void check() {
        if (submit == null) {
            Verify.verify(source.label() == Label.OPTIONAL,
                    String.format("cat not find required field:[%s#%s]", parent.name(), source.name()));
            return;
        }

        /* field的位置不应该变动 */
        Verify.verify(source.tag() == submit.tag(),
                String.format("the field:[%s#%s] position modified, source is:[%s], submit is:[%s]",
                        parent.name(),
                        source.name(),
                        source.tag(),
                        submit.tag()));

        /* field的label不应该变动 */
        Verify.verify(source.label() == submit.label(),
                String.format("the field:[%s#%s] label modified, source is:[%s], submit is:[%s]",
                        parent.name(),
                        source.name(),
                        source.label(),
                        submit.label()));

        /* field的类型不应该变动 */
        Verify.verify(source.type().toString().equals(submit.type().toString()), String.format(
                "the field:[%s#%s] type modified, source is:[%s], submit is:[%s]",
                parent.name(),
                source.name(),
                source.type().toString(),
                submit.type().toString()));
    }
}
