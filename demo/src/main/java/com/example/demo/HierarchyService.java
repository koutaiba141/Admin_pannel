package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class HierarchyService {
    @Autowired
    private HierarchyRepository hierarchyRepository;

    public List<Hierarchy> findAll() {
        return hierarchyRepository.findAll();
    }

    public List<Hierarchy> findAllActive() {
        return hierarchyRepository.findByActiveTrue();
    }

    public List<Hierarchy> findRootHierarchies() {
        return hierarchyRepository.findByParentIsNull();
    }

    public List<Hierarchy> findChildrenByParentId(Long parentId) {
        return hierarchyRepository.findChildrenByParentId(parentId);
    }

    public List<Hierarchy> findByLevel(String level) {
        return hierarchyRepository.findByLevelAndActiveTrue(level);
    }

    public Optional<Hierarchy> findById(Long id) {
        return hierarchyRepository.findById(id);
    }

    public Hierarchy save(Hierarchy hierarchy) {
        System.out.println("=== Saving Hierarchy ===");
        System.out.println("ID: " + hierarchy.getId());
        System.out.println("Name: " + hierarchy.getName());
        System.out.println("Level: " + hierarchy.getLevel());
        System.out.println("Parent ID: " + hierarchy.getParentId());
        System.out.println("Parent object: " + hierarchy.getParent());
        System.out.println("User ID: " + hierarchy.getUserId());
        
        // Validate that the name is unique
        if (hierarchy.getId() == null && hierarchyRepository.existsByName(hierarchy.getName())) {
            System.err.println("Name already exists: " + hierarchy.getName());
            throw new RuntimeException("Hierarchy with name '" + hierarchy.getName() + "' already exists");
        }
        
        // Set default userId if not provided (for backward compatibility)
        if (hierarchy.getUserId() == null) {
            hierarchy.setUserId(1L); // Default to user ID 1
            System.out.println("Setting default userId to 1");
        }
        
        // Handle parent relationship
        if (hierarchy.getParentId() != null) {
            System.out.println("Looking for parent with ID: " + hierarchy.getParentId());
            Optional<Hierarchy> parentOpt = hierarchyRepository.findById(hierarchy.getParentId());
            if (parentOpt.isPresent()) {
                hierarchy.setParent(parentOpt.get());
                System.out.println("Found parent: " + parentOpt.get().getName());
            } else {
                System.err.println("Parent not found with ID: " + hierarchy.getParentId());
                throw new RuntimeException("Parent hierarchy with ID " + hierarchy.getParentId() + " not found");
            }
        } else {
            hierarchy.setParent(null);
            System.out.println("No parent specified");
        }
        
        // If this is a root hierarchy, ensure parent is null
        if ("ROOT".equals(hierarchy.getLevel())) {
            hierarchy.setParent(null);
            System.out.println("Root hierarchy - setting parent to null");
        }
        
        // Set the hierarchy field to match the name (for backward compatibility)
        if (hierarchy.getHierarchy() == null) {
            hierarchy.setHierarchy(hierarchy.getName());
        }
        
        System.out.println("About to save hierarchy...");
        Hierarchy saved = hierarchyRepository.save(hierarchy);
        System.out.println("Successfully saved hierarchy with ID: " + saved.getId());
        return saved;
    }

    public void deleteById(Long id) {
        // Check if hierarchy has children
        List<Hierarchy> children = hierarchyRepository.findChildrenByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("Cannot delete hierarchy with children. Please delete children first.");
        }
        
        hierarchyRepository.deleteById(id);
    }

    public Hierarchy deactivateById(Long id) {
        Optional<Hierarchy> hierarchyOpt = hierarchyRepository.findById(id);
        if (hierarchyOpt.isPresent()) {
            Hierarchy hierarchy = hierarchyOpt.get();
            hierarchy.setActive(false);
            return hierarchyRepository.save(hierarchy);
        }
        throw new RuntimeException("Hierarchy not found with ID: " + id);
    }

    public List<HierarchyTreeDTO> getHierarchyTree() {
        List<Hierarchy> rootHierarchies = hierarchyRepository.findByParentIsNull();
        List<HierarchyTreeDTO> treeDTOs = new ArrayList<>();
        
        for (Hierarchy root : rootHierarchies) {
            HierarchyTreeDTO rootDTO = buildHierarchyTreeDTO(root);
            treeDTOs.add(rootDTO);
        }
        
        return treeDTOs;
    }
    
    public long count() {
        return hierarchyRepository.count();
    }
    
    private HierarchyTreeDTO buildHierarchyTreeDTO(Hierarchy parent) {
        HierarchyTreeDTO parentDTO = new HierarchyTreeDTO(parent);
        List<Hierarchy> children = hierarchyRepository.findChildrenByParentId(parent.getId());
        
        for (Hierarchy child : children) {
            HierarchyTreeDTO childDTO = buildHierarchyTreeDTO(child);
            parentDTO.addChild(childDTO);
        }
        
        return parentDTO;
    }
} 