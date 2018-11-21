package com.nokia.logtools.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZipUtil {

    /**
     * 压缩数据并转换为BASE64编码字符串
     *
     * @param s 被压缩的字符串
     * @return 压缩后的base64字符串
     */
    public static String compress(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return Base64.encodeBase64String(compress(s.getBytes()));
    }

    /**
     * 压缩数组
     *
     * @param data 字节流数字
     * @return 被压缩后的字节流数组
     */
    public static byte[] compress(byte[] data) {
        byte[] bytes = null;
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length)) {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            bytes = bos.toByteArray();
        } catch (Exception e) {
            bytes = data;
            e.printStackTrace();
        }
        compresser.end();
        return bytes;
    }


    /**
     * 解压BASE64编码后的压缩字符串
     *
     * @param base64str BASE64编码后的压缩字符串
     * @return 解压后的字符串
     */
    public static String decompress(String base64str) {
        if (StringUtils.isBlank(base64str)) {
            return null;
        }
        byte[] bytes = Base64.decodeBase64(base64str);
        return new String(decompress(bytes));
    }

    /**
     * 解压数组
     *
     * @param data 被压缩的数组
     * @return 解压缩后的数组
     */
    public static byte[] decompress(byte[] data) {
        byte[] output = null;
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);
        try (ByteArrayOutputStream o = new ByteArrayOutputStream(data.length)) {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        }
        decompresser.end();
        return output;
    }
}
