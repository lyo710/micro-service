package com.baidu.ub.msoa.container.support.governance.domain.model.registry;

import com.baidu.ub.msoa.container.support.governance.contact.ContactConflictException;
import com.baidu.ub.msoa.container.support.governance.contact.proto.ProtoFileConflictChecker;
import com.baidu.ub.msoa.container.support.governance.contact.proto.ServiceStubJarGenerator;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.ProtoParser;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.util.Objects;

/**
 * Created by pippo on 15/8/3.
 */
public class ProtoContact implements ServiceContact {

    public ProtoContact() {

    }

    public ProtoContact(String schema) {
        this.schema = schema;
    }

    @Override
    public void isConflict(ServiceContact contact) throws ContactConflictException {
        if (contact == null || !(contact instanceof ProtoContact)) {
            throw new ContactConflictException(String.format("expect proto contact but type is:[%s]", contact));
        }

        ProtoFile source = protoFile();
        ProtoFile submit = ((ProtoContact) contact).protoFile();
        new ProtoFileConflictChecker(source, submit).check();
    }

    @Override
    public File stubJar() throws Exception {
        return new ServiceStubJarGenerator(schema).generateJar();
    }

    public ProtoFile protoFile() {
        return ProtoParser.parse("", schema);
    }

    private String schema;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("schema", schema.replaceAll("\n", ""))
                // .append("schema", schema)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProtoContact)) {
            return false;
        }
        ProtoContact that = (ProtoContact) o;
        return Objects.equals(schema, that.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema);
    }
}
