package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.squareup.protoparser.ProtoFile;

/**
 * Created by pippo on 15/8/17.
 */
public class MethodStubFileExtractorMain {

    private static ThreadLocal<ProtoFile[]> methodStubFile = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        MethodStubFileExtractor generator = new MethodStubFileExtractor(args[0], true);
        generator.generate();
        ProtoFile[] files = generator.protoFiles();
        methodStubFile.set(files);
    }

    public static ProtoFile[] extract(String schema) throws Exception {
        MethodStubFileExtractorMain.main(new String[] { schema });
        return methodStubFile.get();
    }

}
