package com.jameswu.demo;

import java.util.ArrayList;
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
        List<Map<String, Object>> rootYaml = new ArrayList<>();
        createUserOrderByBFS(rootYaml);
        generateYaml(rootYaml);
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
            long recommenderId = order / 2;
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
