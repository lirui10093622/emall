package org.emall.common.serialization;

import java.lang.reflect.Type;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public interface Deserializer {

    <T> T deserialize(String string, Type type);
}