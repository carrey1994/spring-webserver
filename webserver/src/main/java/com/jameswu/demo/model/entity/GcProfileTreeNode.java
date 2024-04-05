package com.jameswu.demo.model.entity;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GcProfileTreeNode implements Serializable {
	private UserProfile userProfile;
	private List<GcProfileTreeNode> childrenProfiles;
}
