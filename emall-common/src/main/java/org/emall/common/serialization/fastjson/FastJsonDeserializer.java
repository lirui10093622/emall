package org.emall.common.serialization.fastjson;

import com.alibaba.fastjson.JSON;
import org.emall.common.serialization.Deserializer;

import java.lang.reflect.Type;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public class FastJsonDeserializer implements Deserializer {

    @Override
    public <T> T deserialize(String string, Type type) {
        return JSON.parseObject(string, type);
    }
}