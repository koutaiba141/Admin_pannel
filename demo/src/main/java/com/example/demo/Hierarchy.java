package com.example.demo;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "hierarchy")
public class Hierarchy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String hierarchy; // This field was missing from the old schema

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Hierarchy parent;

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;

    @Column(nullable = false)
    private String level; // e.g., "ROOT", "BRANCH", "DEPARTMENT", "TEAM"

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Long userId;

    public Hierarchy() {}

    public Hierarchy(String name, String description, Hierarchy parent, String level) {
        this.name = name;
        this.description = description;
        this.parent = parent;
        this.level = level;
        this.active = true;
    }

    public Hierarchy(String name, String description, Hierarchy parent, String level, Long userId) {
        this.name = name;
        this.description = description;
        this.parent = parent;
        this.level = level;
        this.active = true;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHierarchy() { return hierarchy; }
    public void setHierarchy(String hierarchy) { this.hierarchy = hierarchy; }

    public Hierarchy getParent() { return parent; }
    public void setParent(Hierarchy parent) { this.parent = parent; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Hierarchy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parentId=" + parentId +
                ", level='" + level + '\'' +
                ", active=" + active +
                ", userId=" + userId +
                '}';
    }
} 