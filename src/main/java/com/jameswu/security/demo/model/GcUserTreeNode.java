package com.jameswu.security.demo.model;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GcUserTreeNode {

    private UUID ancestorId;

    private List<GcUserTreeNode> descendants;
}
