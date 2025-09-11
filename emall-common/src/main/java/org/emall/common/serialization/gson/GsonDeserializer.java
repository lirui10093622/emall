package org.emall.common.serialization.gson;

import com.google.gson.Gson;
import org.emall.common.serialization.Deserializer;

import java.lang.reflect.Type;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public class GsonDeserializer implements Deserializer {

    @Override
    public <T> T deserialize(String string, Type type) {
        return new Gson().fromJson(string, type);
    }
}