package org.emall.common.serialization.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.emall.common.serialization.Deserializer;

import java.lang.reflect.Type;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public class JacksonDeserializer implements Deserializer {

    @Override
    public <T> T deserialize(String string, Type type) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(string, mapper.getTypeFactory().constructType(type));
    }
}
