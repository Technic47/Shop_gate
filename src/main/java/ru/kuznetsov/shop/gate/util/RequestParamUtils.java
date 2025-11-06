package ru.kuznetsov.shop.gate.util;

import java.util.Map;

public class RequestParamUtils {

    public static String getParamStringValue(Map<String, String> reqParam, String paramName) {
        String value = null;

        if (reqParam != null
                && !reqParam.isEmpty()
                && reqParam.containsKey(paramName)) {
            value = reqParam.get(paramName);
        }

        return value;
    }

    public static Long getParamLongValue(Map<String, String> reqParam, String paramName) {
        String paramStringValue = getParamStringValue(reqParam, paramName);
        return paramStringValue == null ? null : Long.parseLong(paramStringValue);
    }
}
