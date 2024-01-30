package com.jameswu.security.demo.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GcProfileTreeNode {

    private UserProfile userProfile;
    private List<GcProfileTreeNode> descendantProfiles;
}
