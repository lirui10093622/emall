package org.emall.common.serialization.fastjson;

import com.alibaba.fastjson.JSON;
import org.emall.common.serialization.Serializer;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public class FastJsonSerializer implements Serializer {

    @Override
    public <T> String serialize(T t) {
        return JSON.toJSONString(t);
    }
}