package com.victor.backend.projects.orderSystem.util;

public class StringUtil {

    public static boolean isInt(String s) {
        if (s == null) return false;

        return s.matches("-?[1-9]\\d*|0");
    }

    public static boolean isNumberic(String s) {
        if (s == null) return false;

        return s.matches("-?([1-9]\\d*|0)(\\.[0-9]+)?");
    }

}
