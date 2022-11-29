package com.ghl.sse.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * packageName    : com.ghl.sse.type
 * fileName       : NotificationType
 * author         : lgh
 * date           : 2022/11/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/29        lgh       최초 생성
 */
@Getter
@AllArgsConstructor
public enum NotificationType {
    SUBSCRIBE("SUBSCRIBE" , "SUBSCRIBE SUCCESS"),
    RECEIVING("RECEIVING", "입고 알림"),
    STOCK("STOCK", "재고 알림"),
    ORDER("ORDER", "주문 알림");

    private final String value;
    private final String description;

    public static List<Map> getEnumToListMap() {
        List<Map> resultList = new ArrayList<>();
        for (NotificationType type : NotificationType.values()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", type.getValue());
            resultList.add(map);
        }

        return resultList;
    }

    @JsonValue
    public String getValue() {
        return value;
    }


}


