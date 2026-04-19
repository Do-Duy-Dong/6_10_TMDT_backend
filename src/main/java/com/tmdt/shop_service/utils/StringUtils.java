package com.tmdt.shop_service.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class StringUtils {
    public static String likeLowerContentString(String str) {
        if (str == null) return "%%";
        if (str.trim().isEmpty()) return "%%";
        return "%" + str.toLowerCase().trim() + "%";
    }

    public static String genDirection(List<String> sortFields, Pageable pageable) {
        String paging = "order by ";
        boolean hasSort = false;
        pageable.getSort();
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                if (sortFields.contains(order.getProperty())) {
                    paging += order.getProperty() + " " + order.getDirection() + ", ";
                    hasSort = true;
                }
            }
        }
        if (hasSort) {
            paging = paging.substring(0, paging.length() - 2) + "\n";
        } else {
            paging = "order by create_at desc\n";
        }
        paging += "limit " + pageable.getPageSize() + " offset " + pageable.getOffset() + "\n";
        return paging;
    }
}
