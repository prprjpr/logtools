package com.nokia.logtools.encrypt;

import java.util.UUID;

public class Random {

    private static Long count = 0L;
    private static String seed = UUID.randomUUID().toString();

    public static String value() {
        return SHA.sha256(seed + ++count);
    }

}
