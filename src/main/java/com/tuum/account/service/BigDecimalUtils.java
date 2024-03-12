package com.tuum.account.service;

import java.math.BigDecimal;

public class BigDecimalUtils {
    public static long convertBigDecimalToLong(BigDecimal bigDecimal) {
        int scale = bigDecimal.scale();
        long magnitude = (long) Math.pow(10, scale);

        BigDecimal scaledDecimal = bigDecimal.multiply(BigDecimal.valueOf(magnitude));

        return scaledDecimal.longValue();
    }
}
