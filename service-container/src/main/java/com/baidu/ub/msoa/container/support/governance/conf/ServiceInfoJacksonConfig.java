package com.baidu.ub.msoa.container.support.governance.conf;

import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ProtoContact;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ServiceContact;
import com.baidu.ub.msoa.utils.JSONUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by pippo on 15/8/19.
 */
@Configuration
public class ServiceInfoJacksonConfig {

    static {
        SimpleModule module = new SimpleModule("ServiceInfo");
        module.addDeserializer(ServiceContact.class, new ServiceContactDeserializer());
        JSONUtil.OBJECT_MAPPER.registerModule(module);
    }

    private static class ServiceContactDeserializer extends JsonDeserializer<ServiceContact> {

        @Override
        public ServiceContact deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return parser.readValueAs(ProtoContact.class);
        }
    }

}
