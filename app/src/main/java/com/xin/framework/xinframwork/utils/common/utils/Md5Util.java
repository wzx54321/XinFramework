package com.xin.framework.xinframwork.utils.common.utils;




import com.xin.framework.xinframwork.utils.common.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 版权所有  保留所有权利。<br>
 * 项目名： <br>
 * 描述：
 *
 * @author Xin
 * @data 2016/1/15
 * @since JDK1.8
 */
public class Md5Util {

    private Md5Util() {
    }

    private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String toHexString(byte[] bytes) {
        if (bytes == null)
            return "";
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(hexDigits[(b >> 4) & 0x0F]);
            hex.append(hexDigits[b & 0x0F]);
        }
        return hex.toString();
    }

    public static String md5(File file) throws IOException {
        MessageDigest messagedigest;
        FileInputStream in = null;
        FileChannel ch = null;
        byte[] encodeBytes = null;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY,
                                                 0,
                                                 file.length());
            messagedigest.update(byteBuffer);
            encodeBytes = messagedigest.digest();
        } catch (NoSuchAlgorithmException neverHappened) {
            throw new RuntimeException(neverHappened);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(ch);
        }

        return toHexString(encodeBytes);
    }

    public static String md5(String string) {
        byte[] encodeBytes;
        try {
            encodeBytes = MessageDigest.getInstance("MD5")
                                       .digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException neverHappened) {
            throw new RuntimeException(neverHappened);
        }

        return toHexString(encodeBytes);
    }
}
