package org.sperri.hadoop;

import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author jie zhao
 * @date 2020/4/15 20:34
 */
public class Test {

    public static void main(String[] args) {

        String s1 = Integer.toBinaryString(192);
        String s2 = Integer.toBinaryString(255);
        System.out.println(254 & 1);

        /*Scanner scanner = new Scanner(System.in);
        int num = Integer.parseInt(scanner.nextLine());
        Map<Integer, Integer> map = new TreeMap<>();
        for (int i=0; i< num; i++) {
            String line = scanner.nextLine();
            String[] splits = line.split("\\s+");
            Integer key = Integer.valueOf(splits[0]);
            Integer value = Integer.valueOf(splits[1]);
            if (map.containsKey(key)) {
                Integer sum = map.get(key) + value;
                map.put(key, sum);
            }else {
                map.put(key, value);
            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/
    }
}
