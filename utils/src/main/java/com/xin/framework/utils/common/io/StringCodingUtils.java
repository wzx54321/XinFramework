package com.xin.framework.utils.common.io;

import android.os.Build;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author MaTianyu
 * @date 2014-12-05
 */
public class StringCodingUtils {

    public static byte[] getBytes(String src, Charset charSet) {
        return src.getBytes(charSet);
    }

}
