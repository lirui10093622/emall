package org.emall.common.serialization.common;

import java.nio.charset.Charset;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public class StringBinary {

    public static byte[] bytes(String string, Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    public static String string(byte[] bytes, Charset charset) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }
}