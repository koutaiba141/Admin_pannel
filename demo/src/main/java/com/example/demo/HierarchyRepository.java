package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HierarchyRepository extends JpaRepository<Hierarchy, Long> {
    
    // Find all root hierarchies (no parent)
    List<Hierarchy> findByParentIsNull();
    
    // Find all hierarchies by parent
    List<Hierarchy> findByParentId(Long parentId);
    
    // Find all hierarchies by level
    List<Hierarchy> findByLevel(String level);
    
    // Find all active hierarchies
    List<Hierarchy> findByActiveTrue();
    
    // Find hierarchies by level and active status
    List<Hierarchy> findByLevelAndActiveTrue(String level);
    
    // Find children of a specific hierarchy
    @Query("SELECT h FROM Hierarchy h WHERE h.parent.id = :parentId")
    List<Hierarchy> findChildrenByParentId(@Param("parentId") Long parentId);
    
    // Find all descendants of a hierarchy (recursive)
    @Query("SELECT h FROM Hierarchy h WHERE h.parent.id = :parentId OR h.parent.parent.id = :parentId OR h.parent.parent.parent.id = :parentId")
    List<Hierarchy> findDescendantsByParentId(@Param("parentId") Long parentId);
    
    // Check if name exists
    boolean existsByName(String name);
    
    // Find by name
    Hierarchy findByName(String name);
} 