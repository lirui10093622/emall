package org.emall.common.serialization;

import org.emall.common.serialization.common.StringBinary;
import org.emall.common.serialization.fastjson.FastJsonDeserializer;
import org.emall.common.serialization.fastjson.FastJsonSerializer;
import org.emall.common.serialization.gson.GsonDeserializer;
import org.emall.common.serialization.gson.GsonSerializer;
import org.emall.common.serialization.jackson.JacksonDeserializer;
import org.emall.common.serialization.jackson.JacksonSerializer;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public class Serialization {

    private static final Serializer DEFAULT_SERIALIZER = new GsonSerializer();
    private static final Deserializer DEFAULT_DESERIALIZER = new GsonDeserializer();
    private static final Map<SerializationType, Serializer> serializers;
    private static final Map<SerializationType, Deserializer> deserializers;

    static {
        Map<SerializationType, Serializer> tempSerializerMap = new HashMap<>();
        tempSerializerMap.put(SerializationType.DEFAULT, DEFAULT_SERIALIZER);
        tempSerializerMap.put(SerializationType.GSON, new GsonSerializer());
        tempSerializerMap.put(SerializationType.FAST_JSON, new FastJsonSerializer());
        tempSerializerMap.put(SerializationType.JACKSON, new JacksonSerializer());
        serializers = Collections.unmodifiableMap(tempSerializerMap);

        Map<SerializationType, Deserializer> tempDeserializerMap = new HashMap<>();
        tempDeserializerMap.put(SerializationType.DEFAULT, DEFAULT_DESERIALIZER);
        tempDeserializerMap.put(SerializationType.GSON, new GsonDeserializer());
        tempDeserializerMap.put(SerializationType.FAST_JSON, new FastJsonDeserializer());
        tempDeserializerMap.put(SerializationType.JACKSON, new JacksonDeserializer());
        deserializers = Collections.unmodifiableMap(tempDeserializerMap);
    }

    public static <T> String serialize(T t) {
        return DEFAULT_SERIALIZER.serialize(t);
    }

    public static <T> byte[] serialize(T t, Charset charset) {
        return StringBinary.bytes(serialize(t), charset);
    }

    public static Serializer serializer() {
        return DEFAULT_SERIALIZER;
    }

    public static Serializer serializer(SerializationType type) {
        return serializers.getOrDefault(type, DEFAULT_SERIALIZER);
    }

    public static <T> T deserialize(String string, Type type) {
        return DEFAULT_DESERIALIZER.deserialize(string, type);
    }

    public static <T> T deserialize(byte[] bytes, Charset charset, Type type) {
        return deserialize(StringBinary.string(bytes, charset), type);
    }

    public static Deserializer deserializer() {
        return DEFAULT_DESERIALIZER;
    }

    public static Deserializer deserializer(SerializationType type) {
        return deserializers.getOrDefault(type, DEFAULT_DESERIALIZER);
    }
}