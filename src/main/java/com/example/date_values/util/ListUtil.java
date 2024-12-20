package com.example.date_values.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    public static boolean hasCommonElement(List<?> list1, List<?> list2) {
        List<?> copyList1 = new ArrayList<>(list1);
        copyList1.retainAll(list2);
        return !copyList1.isEmpty();
    }

    public static List<Integer> getLastTwoCharsAsIntegers(List<String> strings) {
        List<Integer> result = new ArrayList<>();

        for (String str : strings) {
            if (str.length() >= 2) {
                // Lấy 2 ký tự cuối
                String lastTwoChars = str.substring(str.length() - 2);
                try {
                    // Chuyển đổi thành số nguyên
                    result.add(Integer.parseInt(lastTwoChars));
                } catch (NumberFormatException e) {
                    // Xử lý nếu không thể chuyển đổi
                    System.out.println("Không thể chuyển đổi: " + lastTwoChars);
                }
            }
        }

        return result;
    }
}
