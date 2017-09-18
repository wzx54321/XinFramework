package com.xin.framework.xinframwork.store.entity;

import com.xin.framework.xinframwork.utils.android.logger.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.NameInDb;
import io.objectbox.annotation.Transient;
import okhttp3.Cookie;


/**
 * Description :Cookie数据库实体
 * Created by 王照鑫 on 2017/9/14 0014.
 */
@Entity
public class EntityCookie implements Serializable {

    @Id
    private long id;
    @Index
    private String host;
    private String name;
    private String domain;

    @NameInDb("cookie")
    private byte[] cookieByte;

    private String cookieToken;


    @Transient
    private transient Cookie cookie;
    @Transient
    private transient Cookie clientCookie;


    public EntityCookie(String host, Cookie cookie) {
        this.cookie = cookie;
        this.host = host;
        this.name = cookie.name();
        this.domain = cookie.domain();
        this.cookieToken=cookie.name() + "@" + cookie.domain();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public byte[] getCookieByte() {
        return cookieByte;
    }

    public void setCookieByte() {
        this.cookieByte = cookieToBytes(this.host, this.cookie);
    }


    public Cookie getCookie() {
        Cookie bestCookie = cookie;
        if (clientCookie != null) {
            bestCookie = clientCookie;
        }
        return bestCookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
        this.name = cookie.name();
        this.domain = cookie.domain();
        setCookieByte();
    }


    public String getCookieToken() {
        return cookieToken;
    }

    public void setCookieToken(Cookie cookie) {
        this.cookieToken = cookie.name() + "@" + cookie.domain();
    }

    public static byte[] cookieToBytes(String host, Cookie cookie) {
        EntityCookie serializableCookie = new EntityCookie(host, cookie);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(serializableCookie);
        } catch (IOException e) {
            Log.e(e, "cookieToBytes");
            return null;
        }
        return os.toByteArray();
    }




    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(cookie.name());
        out.writeObject(cookie.value());
        out.writeLong(cookie.expiresAt());
        out.writeObject(cookie.domain());
        out.writeObject(cookie.path());
        out.writeBoolean(cookie.secure());
        out.writeBoolean(cookie.httpOnly());
        out.writeBoolean(cookie.hostOnly());
        out.writeBoolean(cookie.persistent());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        long expiresAt = in.readLong();
        String domain = (String) in.readObject();
        String path = (String) in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();
        boolean persistent = in.readBoolean();
        Cookie.Builder builder = new Cookie.Builder();
        builder = builder.name(name);
        builder = builder.value(value);
        builder = builder.expiresAt(expiresAt);
        builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
        builder = builder.path(path);
        builder = secure ? builder.secure() : builder;
        builder = httpOnly ? builder.httpOnly() : builder;
        clientCookie = builder.build();
    }





    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    public static Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        return bytesToCookie(bytes);
    }

    public static Cookie bytesToCookie(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((EntityCookie) objectInputStream.readObject()).getCookie();
        } catch (Exception e) {
            Log.e(e,"bytesToCookie");
        }
        return cookie;
    }


    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }


    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化
     * @return 序列化之后的string
     */
    public static String encodeCookie(String host, Cookie cookie) {
        if (cookie == null) return null;
        byte[] cookieBytes = cookieToBytes(host, cookie);
        return byteArrayToHexString(cookieBytes);
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }
}
