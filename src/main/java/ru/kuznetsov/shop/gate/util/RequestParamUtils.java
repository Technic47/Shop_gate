package ru.kuznetsov.shop.gate.util;

import java.util.Map;
import java.util.UUID;

public class RequestParamUtils {

    public static final String ID_PARAMETER = "id";
    public static final String NAME_PARAMETER = "name";
    public static final String ADDRESS_ID_PARAMETER = "addressId";
    public static final String ORDER_ID_PARAMETER = "orderId";
    public static final String PRODUCT_ID_PARAMETER = "productId";
    public static final String STORE_ID_PARAMETER = "storeId";
    public static final String OWNER_ID_PARAMETER = "ownerId";
    public static final String CATEGORY_ID_PARAMETER = "categoryId";

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

    public static UUID getParamUUIDValue(Map<String, String> reqParam, String paramName) {
        String paramStringValue = getParamStringValue(reqParam, paramName);
        return paramStringValue == null ? null : UUID.fromString(paramStringValue);
    }
}
