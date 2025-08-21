package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hierarchies")
public class HierarchyController {
    
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
    
    @PostMapping("/ping")
    public String pingPost(@RequestBody(required = false) String body) {
        System.out.println("POST ping received with body: " + body);
        return "post pong";
    }
    
    @PostMapping("/test-simple")
    public ResponseEntity<String> testSimple(@RequestBody TestObject testObject) {
        System.out.println("Test object received: " + testObject);
        return ResponseEntity.ok("Received: " + testObject.getName());
    }
    
    public static class TestObject {
        private String name;
        private String description;
        
        public TestObject() {}
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        @Override
        public String toString() {
            return "TestObject{name='" + name + "', description='" + description + "'}";
        }
    }
    @Autowired
    private HierarchyService hierarchyService;

    @GetMapping
    public List<Hierarchy> getAll() {
        return hierarchyService.findAllActive();
    }

    @GetMapping("/all")
    public List<Hierarchy> getAllIncludingInactive() {
        return hierarchyService.findAll();
    }

    @GetMapping("/tree")
    public List<HierarchyTreeDTO> getHierarchyTree() {
        return hierarchyService.getHierarchyTree();
    }

    @GetMapping("/root")
    public List<Hierarchy> getRootHierarchies() {
        return hierarchyService.findRootHierarchies();
    }

    @GetMapping("/level/{level}")
    public List<Hierarchy> getByLevel(@PathVariable String level) {
        return hierarchyService.findByLevel(level);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hierarchy> getById(@PathVariable Long id) {
        Optional<Hierarchy> hierarchy = hierarchyService.findById(id);
        return hierarchy.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/children")
    public List<Hierarchy> getChildren(@PathVariable Long id) {
        return hierarchyService.findChildrenByParentId(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Hierarchy hierarchy) {
        System.out.println("=== Creating Hierarchy ===");
        System.out.println("Hierarchy object: " + hierarchy);
        
        if (hierarchy == null) {
            System.err.println("Hierarchy object is null - JSON parsing failed");
            return ResponseEntity.badRequest().body("Error: Invalid JSON or missing required fields");
        }
        
        System.out.println("Name: " + hierarchy.getName());
        System.out.println("Description: " + hierarchy.getDescription());
        System.out.println("Level: " + hierarchy.getLevel());
        System.out.println("Parent ID: " + hierarchy.getParentId());
        System.out.println("Active: " + hierarchy.getActive());
        System.out.println("User ID: " + hierarchy.getUserId());
        
        try {
            Hierarchy saved = hierarchyService.save(hierarchy);
            System.out.println("Successfully created hierarchy with ID: " + saved.getId());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("Error creating hierarchy: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Hierarchy update(@PathVariable Long id, @RequestBody Hierarchy hierarchy) {
        hierarchy.setId(id);
        System.out.println("Updating hierarchy with ID: " + id);
        return hierarchyService.save(hierarchy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            hierarchyService.deleteById(id);
            return ResponseEntity.ok("Hierarchy deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/deactivate")
    public Hierarchy deactivate(@PathVariable Long id) {
        return hierarchyService.deactivateById(id);
    }
    
    @GetMapping("/test")
    public String test() {
        try {
            long count = hierarchyService.count();
            return "Hierarchy controller is working! Total hierarchies: " + count;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @GetMapping("/db-test")
    public String dbTest() {
        try {
            // Test if we can access the repository
            long count = hierarchyService.count();
            return "Database connection OK. Total hierarchies: " + count;
        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }
} 