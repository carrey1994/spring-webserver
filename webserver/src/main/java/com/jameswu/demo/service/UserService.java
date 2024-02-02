package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.GcProfileLevel;
import com.jameswu.demo.model.entity.GcProfileTreeNode;
import com.jameswu.demo.repository.GcProfileLevelRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    public UserService(GcProfileLevelRepository gcProfileLevelRepository) {
        this.gcProfileLevelRepository = gcProfileLevelRepository;
    }

    private final GcProfileLevelRepository gcProfileLevelRepository;

    public GcProfileTreeNode searchingDiagram(Long userId) {
        return queryByUserIdAndLevel(userId, 4);
    }

    public GcProfileTreeNode searchingChildren(Long userId) {
        return queryByUserIdAndLevel(userId, 1);
    }

    private GcProfileTreeNode queryByUserIdAndLevel(Long userId, int level) {
        List<GcProfileLevel> gcProfileLevelList = gcProfileLevelRepository.queryChildren(userId, level);
        if (gcProfileLevelList.size() == 1) {
            // no any child, return user only.
            return new GcProfileTreeNode(gcProfileLevelList.get(0).toUserProfile(), new ArrayList<>());
        }
        return mappingChildren(gcProfileLevelList, userId);
    }

    private GcProfileTreeNode mappingChildren(List<GcProfileLevel> gcProfileLevelList, long userId) {
        GcProfileLevel rootProfileLevel = gcProfileLevelList.stream()
                .filter((profileLevel) -> profileLevel.getUserId() == userId)
                .toList()
                .get(0);
        Map<Long, List<GcProfileLevel>> collect = gcProfileLevelList.stream()
                .filter(e -> e.getRecommenderId() != null)
                .collect(Collectors.groupingBy(GcProfileLevel::getRecommenderId));
        List<GcProfileTreeNode> children = collect.get(userId).stream()
                .map(e -> new GcProfileTreeNode(e.toUserProfile(), new ArrayList<>()))
                .toList();
        GcProfileTreeNode rootNode = new GcProfileTreeNode(rootProfileLevel.toUserProfile(), children);
        createTreeDiagram(collect, rootNode);
        return rootNode;
    }

    private void createTreeDiagram(Map<Long, List<GcProfileLevel>> collect, GcProfileTreeNode node) {
        if (collect.get(node.getUserProfile().getUserId()) == null) {
            return;
        }
        List<GcProfileTreeNode> children = collect.get(node.getUserProfile().getUserId()).stream()
                .map(e -> new GcProfileTreeNode(e.toUserProfile(), new ArrayList<>()))
                .toList();
        node.setChildrenProfiles(children);
        for (GcProfileTreeNode child : children) {
            createTreeDiagram(collect, child);
        }
    }
}
