package com.nokia.logtools.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * gzip压缩工具.
 */
public class Gzip {

    /**
     * 压缩数据并转换为BASE64编码字符串.
     *
     * @param String
     * @returnString
     */
    public static String compress(String source) {
        if (StringUtils.isBlank(source)) {
            return "";
        }
        try {
            byte[] bytes = compress(source.getBytes("UTF-8"));
            return Base64.encodeBase64String(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] compress(byte[] source) {
        if (source == null || source.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(source);
            gzip.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
        return out.toByteArray();
    }

    /**
     * 解压BASE64编码后的压缩数据.
     *
     * @param source BASE64编码过的压缩数据
     * @return 原始数据
     * @throws IOException
     */
    public static String decompress(String base64str) {
        byte[] bytes = Base64.decodeBase64(base64str);
        try {
            return new String(decompress(bytes), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解压
     *
     * @param data 源数据
     * @return 解压后数据
     * @throws IOException
     */
    public static byte[] decompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
        }
        return out.toByteArray();
    }

}
