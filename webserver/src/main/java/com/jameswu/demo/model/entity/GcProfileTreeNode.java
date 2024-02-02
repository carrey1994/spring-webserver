package com.jameswu.demo.model.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GcProfileTreeNode {
    private UserProfile userProfile;
    private List<GcProfileTreeNode> childrenProfiles;
}
