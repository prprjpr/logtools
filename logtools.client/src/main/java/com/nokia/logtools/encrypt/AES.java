package com.nokia.logtools.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ResourceBundle;

/**
 * AES 加密解密 Created by li on 05/25/18.
 */
public class AES {
    private static final String AES_CONST = "AES";
    /**
     * AES 算法 CBC
     */
    private static final String AES_ALG_CBC = "AES/CBC/PKCS5Padding";// "算法/模式/补码方式"

    /**
     * AES算法 分组长度
     */
    private static final int AES_LENGTH = 128;

    private static final int BLOCK_SIZE = 16;

    @SuppressWarnings("unused")
    private static String aesAlg;

    // 初始化参数
    static {
        try {
            ResourceBundle resource = ResourceBundle.getBundle("config");
            aesAlg = resource.getString("alg_aes");
        } catch (Exception e) {
            aesAlg = "AES/CBC/PKCS5Padding";
        }
    }

    /**
     * 依赖随机数产生AESkey 目前只开放128位 返回BASE64编码
     *
     * @param random 随机数
     * @return String BASE64编码
     */
    public static String generateKey(String random) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(AES_CONST);
            // kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
            // SecureRandom是生成安全随机数序列，random.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。
            kg.init(AES_LENGTH, new SecureRandom(random.getBytes()));
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            return Base64.encodeBase64String(b);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No Such Algorithm Exception", e);
        }
    }

    /**
     * AES 加密
     *
     * @param key     AES key BASE64编码
     * @param dataStr 待加密数据字符串
     * @return String BASE64 编码
     */
    public static String encryptStr(String key, String dataStr) {
        byte[] data = dataStr.getBytes();
        return Base64.encodeBase64String(encrypt(key, data));
    }

    /**
     * AES CBC 解密
     *
     * @param key         AES key BASE64编码
     * @param encryptData 待解密数据字符串 BASE64 编码
     * @return String 解密结果
     */
    public static String decryptStr(String key, String encryptData) {
        byte[] cryptByte = Base64.decodeBase64(encryptData);
        byte[] origData = decrypt(key, cryptByte);
        return new String(origData);
    }

    /**
     * AES 加密 返回 byte 数组
     *
     * @param key  AES key BASE64编码
     * @param data byte[] 待加密数据
     * @return byte[]
     */
    public static byte[] encrypt(String key, byte[] data) {
        byte[] keyBytes = getKey(key);
        byte[] buf = new byte[BLOCK_SIZE];
        System.arraycopy(keyBytes, 0, buf, 0, keyBytes.length > buf.length ? keyBytes.length : buf.length);
        try {
            Cipher cipher = Cipher.getInstance(AES_ALG_CBC);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, AES_CONST), new IvParameterSpec(buf));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("AES 加密 异常", e);
        }
    }

    /**
     * AES 解密 返回 byte 数组
     *
     * @param key  AES key BASE64编码
     * @param data 已加密数据
     * @return byte[]
     */
    public static byte[] decrypt(String key, byte[] data) {
        byte[] keyBytes = getKey(key);
        byte[] buf = new byte[BLOCK_SIZE];
        System.arraycopy(keyBytes, 0, buf, 0, keyBytes.length > buf.length ? keyBytes.length : buf.length);
        try {
            Cipher cipher = Cipher.getInstance(AES_ALG_CBC);
            // 密钥作为初始化变量
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, AES_CONST), new IvParameterSpec(buf));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("AES 解密异常", e);
        }
    }

    /**
     * 返回密钥
     *
     * @param key BASE64编码
     * @return byte[] 数组
     */
    private static byte[] getKey(String key) {
        byte[] keyBytes = Base64.decodeBase64(key);
        // 128为公16个字节长度
        if (BLOCK_SIZE != keyBytes.length) {
            throw new RuntimeException("密钥长度不符合,需为128位");
        }
        return keyBytes;
    }
}