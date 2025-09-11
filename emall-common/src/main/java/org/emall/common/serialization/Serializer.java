package org.emall.common.serialization;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public interface Serializer {

    <T> String serialize(T t);
}