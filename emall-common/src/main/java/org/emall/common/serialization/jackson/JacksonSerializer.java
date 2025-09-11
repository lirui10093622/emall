package org.emall.common.serialization.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.emall.common.serialization.Serializer;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public class JacksonSerializer implements Serializer {
    @Override
    public <T> String serialize(T t) {
        return new ObjectMapper().convertValue(t, String.class);
    }
}