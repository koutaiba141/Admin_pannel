package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

public class HierarchyTreeDTO {
    private Long id;
    private String name;
    private String description;
    private String level;
    private Boolean active;
    private Long parentId;
    
    @JsonProperty("children")
    private List<HierarchyTreeDTO> children = new ArrayList<>();

    public HierarchyTreeDTO() {}

    public HierarchyTreeDTO(Hierarchy hierarchy) {
        this.id = hierarchy.getId();
        this.name = hierarchy.getName();
        this.description = hierarchy.getDescription();
        this.level = hierarchy.getLevel();
        this.active = hierarchy.getActive();
        this.parentId = hierarchy.getParentId();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public List<HierarchyTreeDTO> getChildren() { return children; }
    public void setChildren(List<HierarchyTreeDTO> children) { this.children = children; }

    public void addChild(HierarchyTreeDTO child) {
        this.children.add(child);
    }
} 