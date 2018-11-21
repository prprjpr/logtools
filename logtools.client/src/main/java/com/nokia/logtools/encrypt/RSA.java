package com.nokia.logtools.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSA {
    /**
     * RSA算法
     */
    private static final String RSA_ALGORITHM = "RSA";// RSA/ECB/PKCS1Padding
    /**
     * 签名算法
     */
    private static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";
    /**
     * RSA算法字节数
     */
    private static final int RAS_LENGTH = 1024;
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";

    /**
     * 生成一组公钥和私钥 (BASE64编码)
     *
     * @return map 获取私钥方式: map.get(RSAUtils.PRIVATE_KEY) 获取公钥方式: map.get(RSAUtils.PUBLIC_KEY)
     */
    public static Map<String, String> createKeyPair() {
        String publicKey = "";
        String privateKey = "";
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            // 密钥位数
            keyPairGen.initialize(RAS_LENGTH);
            // 密钥对
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 公钥
            PublicKey pubKey = keyPair.getPublic();
            // 私钥
            PrivateKey priKey = keyPair.getPrivate();
            publicKey = getKeyString(pubKey);
            privateKey = getKeyString(priKey);
        } catch (Exception e) {
            throw new RuntimeException("generate RSA key pair error", e);
        }
        Map<String, String> map = new HashMap<>();
        map.put(PUBLIC_KEY, publicKey);
        map.put(PRIVATE_KEY, privateKey);
        return map;
    }

    /**
     * 利用公钥对数据加密 (与“decryptCBCStr(String privateKey, String data)” 配对)
     *
     * @param pubKey (公钥) (BASE64编码)
     * @param data   (待加密的数据)
     * @return String (加密后的数据)BASE64编码)
     */
    public static String encryptStr(String pubKey, String data) {
        byte[] enBytes = encrypt(pubKey, data.getBytes());
        return Base64.encodeBase64String(enBytes);
    }

    /**
     * 利用私钥对数据解密
     *
     * @param privateKey 私钥 (BASE64编码)
     * @param data       加密的数据 (BASE64编码)
     * @return String
     * @throws Exception
     */
    public static String decryptStr(String privateKey, String data) {
        try {
            return new String(decrypt(privateKey, Base64.decodeBase64(data)));
        } catch (Exception e) {
            throw new RuntimeException("decryptCBCStr error", e);
        }
    }

    /**
     * byte 类型 利用公钥对数据加密 (与“decryptCBCStr(String privateKey, byte[] data)” 配对)
     *
     * @param pubKey (公钥) (BASE64编码)
     * @param data   byte[]类型 (待加密的数据)
     * @return byte[]
     * @throws Exception
     */
    public static byte[] encrypt(String pubKey, byte[] data) {
        byte[] enBytes;
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            PublicKey publicKey = getPubKey(pubKey, RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            enBytes = cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("encryptCBCStr error", e);
        }
        return enBytes;
    }

    /**
     * 私钥解密
     *
     * @param priKey BASE64编码)
     * @param data   byte[]
     * @return byte[]
     */
    public static byte[] decrypt(String priKey, byte[] data) {
        byte[] deBytes;
        try {
            // 加解密类
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            // 通过密钥字符串得到密钥
            PrivateKey privateKey = getPriKey(priKey, RSA_ALGORITHM);
            // 解密
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            deBytes = cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("decryptCBCStr error", e);
        }
        return deBytes;
    }

    /**
     * 签名
     *
     * @param priKey private key (BASE64编码)
     * @param data   待签名数据
     * @return String(BASE64编码)
     */
    public static String sign(String priKey, String data) {
        try {
            PrivateKey privateKey = getPriKey(priKey, RSA_ALGORITHM);
            // sign
            Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException("签名字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 签名校验
     *
     * @param publicKey 公钥 (BASE64编码)
     * @param data      原始数据
     * @param sign      签名 BASE64编码
     * @return boolean true 校验成功
     */
    public static boolean verify(String publicKey, String data, String sign) {
        try {
            PublicKey pubKey = getPubKey(publicKey, RSA_ALGORITHM);
            Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
            signature.initVerify(pubKey);
            signature.update(data.getBytes());
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            throw new RuntimeException("验签字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 获取公钥
     *
     * @param key     (BASE64编码)
     * @param rsaCode RSA算法
     * @return PublicKey
     */
    private static PublicKey getPubKey(String key, String rsaCode) {
        PublicKey publicKey;
        try {
            byte[] keyBytes = Base64.decodeBase64(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(rsaCode);
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("get public key error", e);
        }
        return publicKey;
    }

    /**
     * 获取私钥
     *
     * @param key     (BASE64编码)
     * @param rsaCode RSA算法
     * @return PrivateKey
     */
    private static PrivateKey getPriKey(String key, String rsaCode) {
        PrivateKey privateKey;
        try {

            byte[] keyBytes = Base64.decodeBase64(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(rsaCode);
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("get private key error", e);
        }
        return privateKey;
    }

    /**
     * 获取密钥
     *
     * @param key
     * @return String BASE64编码
     */
    private static String getKeyString(Key key) {
        String s = "";
        try {
            byte[] keyBytes = key.getEncoded();
            s = Base64.encodeBase64String(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException("trans to base 64 error", e);
        }
        return s;
    }
}
