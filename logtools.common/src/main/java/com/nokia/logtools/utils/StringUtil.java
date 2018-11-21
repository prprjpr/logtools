package com.nokia.logtools.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * explain: 字符串工具类
 * <p>
 * CreateDate 2018/7.
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {

    /**
     * 分隔符字符串 转换list
     *
     * @param splitStr
     * @param split
     * @return
     */
    public static List<String> getSplitList(String splitStr, String split) {
        if (isEmpty(splitStr) || isEmpty(split)) {
            return null;
        }
        String[] strs = splitStr.split(split);
        if (strs.length == 0) {
            return null;
        }
        return Arrays.asList(strs);
    }

    /**
     * 字符串转换
     *
     * @param obj
     * @return
     */
    public static String objToString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * String 转换 Integer类型
     *
     * @param num
     * @return
     */
    public static Integer strToInteger(String num) {
        if (isEmpty(num)) {
            return 0;
        }
        Integer count = 0;
        try {
            count = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * String 转换 Long 类型
     *
     * @param num
     * @return
     */
    public static Long strToLong(String num) {
        if (isEmpty(num)) {
            return 0L;
        }
        Long count = 0L;
        try {
            count = Long.parseLong(num);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * String 转换 Double类型
     *
     * @param num
     * @return
     */
    public static Double strToDouble(String num) {
        if (isEmpty(num)) {
            return 0.0;
        }
        Double count = 0.0;
        try {
            count = Double.parseDouble(num);
        } catch (NumberFormatException e) {
            return count;
        }
        return count;
    }

    /**
     * String 转换 BigDecimal类型
     *
     * @param num
     * @return
     */
    public static BigDecimal strToBigDecimal(String num) {
        if (isEmpty(num)) {
            return new BigDecimal(0);
        }
        BigDecimal count = new BigDecimal(0);

        try {
            count = new BigDecimal(num);
        } catch (Exception e) {
            return count;
        }
        return count;
    }

    /**
     * 生成32位ID
     *
     * @return
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        return s.replaceAll("-", "").toUpperCase();
    }

    /**
     * 数字补0 字符串
     *
     * @param num
     * @param count
     * @return
     */
    public static String numAddZero(String num, Integer count) {
        Integer numLenght = num.length();
        Integer forCount = count - numLenght;
        String str = "";
        for (int i = 1; i <= forCount; i++) {
            str += "0";
        }
        return str + num;
    }

    /**
     * 生成随机数密码
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 使用StringBuilder 拼凑字符串
     *
     * @param str
     * @return
     */
    public static String builderStr(String... str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i]);
        }
        return sb.toString();
    }

    /**
     * 字符串 过长截取 如：abcde abcd...
     *
     * @param count
     * @param str
     * @param type
     * @return
     */
    public static String omitStr(int count, String str, String type) {
        if (isEmpty(str)) {
            return "";
        }
        return builderStr(str.substring(0, count), type);
    }

    /**
     * 剔除特殊字符
     */
    public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
        if (StringUtil.isEmpty(text)) {
            return "";
        }
        byte[] bytes = text.getBytes("utf-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256; // 去掉符号位
            if (((b >> 5) ^ 0x6) == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            } else if (((b >> 4) ^ 0xE) == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            } else if (((b >> 3) ^ 0x1E) == 0) {
                i += 4;
            } else if (((b >> 2) ^ 0x3E) == 0) {
                i += 5;
            } else if (((b >> 1) ^ 0x7E) == 0) {
                i += 6;
            } else {
                buffer.put(bytes[i++]);// 添加处理如b的指为-48等情况出现的死循环
            }
        }
        buffer.flip();
        return new String(buffer.array(), "utf-8");
    }


    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underlineToCamel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camelToUnderline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    /**
     * 生成非重复订单号，理论上限1毫秒1000个，可扩展
     *
     * @param prefix 订单前缀
     */
    public static String makeOrderNum(String prefix) {
        // 最终生成的订单号
        String finOrderNum = "";
        try {
            synchronized (lockObj) {
                // 取系统当前时间作为订单号变量前半部分，精确到毫秒
                long nowLong = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
                // 计数器到最大值归零，可扩展更大，目前1毫秒处理峰值1000个，1秒100万
                if (orderNumCount >= maxPerMSECSize) {
                    orderNumCount = 0L;
                }
                // 组装订单号
                String countStr = maxPerMSECSize + orderNumCount + "";
                finOrderNum = prefix + nowLong + countStr.substring(1);
                orderNumCount++;
            }
            return finOrderNum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 不传前缀生成订单号
     *
     * @return
     */
    public static String makeOrderNum() {
        return makeOrderNum("");
    }

    /**
     * 锁对象，可以为任意对象
     */
    private static Object lockObj = "lockerOrder";

    /**
     * 订单号生成计数器
     */
    private static long orderNumCount = 0L;

    /**
     * 每毫秒生成订单号数量最大值
     */
    private static int maxPerMSECSize = 1000;

}
