package com.jameswu.demo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import lombok.SneakyThrows;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

class TreeYamlTest {


    @SneakyThrows
    @Test
    void initTree() {
//        new AA().test();
//        var bb = new BB(1);
//        bb.x = 2;
//        var bbb = new BB(2);
//        System.out.println(bbb.x);
        //        List<Map<String, Object>> rootYaml = new ArrayList<>();
        //        createUserOrderByBFS(rootYaml);
        //        generateYaml(rootYaml);
        //		System.out.println(removeDuplicates(new int[] {1, 1, 2}));
    }

    /*
     * reverse-in-place function
     * */
    public void reverseInPlace(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
            left++;
            right--;
        }
        for (int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
        }
    }

    public int removeDuplicates(int[] nums) {
        int p1 = 0;
        int p2 = 1;
        int count = 1;
        while (p1 < nums.length && p2 < nums.length) {
            if (nums[p1] == nums[p2]) {
                p2++;
            } else {
                count++;
                nums[count - 1] = nums[p2];
                p1 = p2;
            }
        }

        for (int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
        }
        return count;
    }

    void createUserOrderByBFS(List<Map<String, Object>> rootYaml) {
        Queue<Long> queue = new LinkedList<>();
        queue.add(1L);
        int order = 1;
        while (!queue.isEmpty()) {
            queue.poll();
            HashMap<String, Object> entry = new LinkedHashMap<>();
            entry.put("id", order);
            entry.put("username", "testuser" + order);
            entry.put("password", "testuser" + order);
            entry.put("role", "ADMIN");
            int recommenderId = order / 2;
            if (recommenderId == 0) {
                entry.put("recommenderId", null);
            } else {
                entry.put("recommenderId", recommenderId);
            }
            rootYaml.add(entry);
            order++;
            queue.addAll(List.of(1L, 1L));
            if (order > 63) break;
        }
    }

    void generateYaml(List<Map<String, Object>> yamlUsers) {
        Map<String, Object> yamlRoot = new LinkedHashMap<>();
        Map<String, Object> init = new LinkedHashMap<>();
        init.put("users", yamlUsers);
        yamlRoot.put("init", init);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        Assertions.assertFalse(Strings.isNullOrEmpty(yaml.dump(yamlRoot)));
    }
}
