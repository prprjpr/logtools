package com.nokia.logtools.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

public class SHA {

    public static String sha256(String data) {
        return sha("SHA-256", data);
    }

    private static String sha(String algorithm, String data) {
        try {
            MessageDigest md = DigestUtils.getDigest(algorithm);
            md.update(data.getBytes("UTF-8"));
            return Base64.encodeBase64String(md.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}