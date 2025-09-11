package org.emall.common.serialization.gson;

import com.google.gson.Gson;
import org.emall.common.serialization.Serializer;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public class GsonSerializer implements Serializer {

    @Override
    public <T> String serialize(T t) {
        return new Gson().toJson(t);
    }
}