package com.dwj.generator.model.file;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PathNode
 * @Description
 * @Author dwjian
 * @Date 2021/3/23 21:42
 */
@Data
@NoArgsConstructor
public class PathNode implements Serializable {
    private String id;
    private String label;
    private Boolean isLeaf;
    private Boolean isOpen = false;
    private List<PathNode> children;

    public PathNode(String id, String label, Boolean isLeaf) {
        this.id = id;
        this.label = label;
        this.isLeaf = isLeaf;
    }
}
