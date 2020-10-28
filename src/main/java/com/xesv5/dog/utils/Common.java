package com.xesv5.dog.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Common {
    // 签名
    public static String getSign(Integer taskid, String timestamp, String token) {
        return DigestUtils.md5Hex(taskid.toString() + "&" + timestamp + token);
    }

    // Unix时间戳（毫秒转秒）
    public static String getCurrentTimeStamp() {
        long timestamp = System.currentTimeMillis() / 1000;
        return String.valueOf(timestamp);
    }
}
