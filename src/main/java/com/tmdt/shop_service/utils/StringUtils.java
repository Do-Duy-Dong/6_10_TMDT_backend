package com.tmdt.shop_service.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class StringUtils {
    public static String likeLowerContentString(String str) {
        if (str == null) return "%%";
        if (str.trim().isEmpty()) return "%%";
        return "%" + str.toLowerCase().trim() + "%";
    }

    public static String genDirection(List<String> sortFields, Pageable pageable, String alias) {
        if (pageable == null) {
            return "";
        }
        String paging = "";
        boolean hasSort = false;
        pageable.getSort();
        if (pageable.getSort().isSorted()) {
            paging = "order by ";
            for (Sort.Order order : pageable.getSort()) {
                if (sortFields.contains(order.getProperty())) {
                    if (alias != null && !alias.isEmpty()) {
                        paging += alias + "." +order.getProperty() + " " + order.getDirection() + ", ";
                    } else {
                        paging += order.getProperty() + " " + order.getDirection() + ", ";
                    }
                    hasSort = true;
                }
            }
        }
        if (hasSort) {
            paging = paging.substring(0, paging.length() - 2) + "\n";
        }
        paging += "limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        return paging;
    }

    public static String generatorSlug(String baseSlug) {
        LocalDateTime now = LocalDateTime.now();
        return baseSlug + "_" + now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
