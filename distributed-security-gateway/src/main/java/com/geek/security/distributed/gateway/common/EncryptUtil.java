package com.geek.security.distributed.gateway.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);

    public static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String encodeUTF8StringBase64(String str) {
        String encoded = null;
        encoded = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
        return encoded;
    }

    public static String decodeUTF8StringBase64(String str) {
        String decoded = null;
        byte[] bytes = Base64.getDecoder().decode(str);
        decoded = new String(bytes, StandardCharsets.UTF_8);
        return decoded;
    }

    public static String encodeURL(String url) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LOGGER.warn("URLEncode 失败。", e);
        }
        return encoded;
    }

    public static String decodeURL(String url) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LOGGER.warn("URLDecode 失败。", e);
        }
        return decoded;
    }

    public static void main(String[] args) {
        String string = "abcd{'a':'b'}";
        System.out.println("string = " + string);
        String encoded = EncryptUtil.encodeUTF8StringBase64(string);
        System.out.println("encoded = " + encoded);
        String decoded = EncryptUtil.decodeUTF8StringBase64(encoded);
        System.out.println("decoded = " + decoded);

        String url = "== wo";
        System.out.println("url = " + url);
        String encodeURL = EncryptUtil.encodeURL(url);
        System.out.println("encodeURL = " + encodeURL);
        String decodeURL = EncryptUtil.decodeURL(encodeURL);
        System.out.println("decodeURL = " + decodeURL);
    }
}

/*
string = abcd{'a':'b'}
encoded = YWJjZHsnYSc6J2InfQ==
decoded = abcd{'a':'b'}
url = == wo
encodeURL = %3D%3D+wo
decodeURL = == wo

Process finished with exit code 0
 */
