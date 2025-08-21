package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ip-config")
public class IpConfigController {
    @Autowired
    private IpConfigService ipConfigService;

    @GetMapping
    public List<IpConfig> getAll() {
        return ipConfigService.findAll();
    }

    @PostMapping
    public ResponseEntity<IpConfig> create(@RequestBody IpConfig ipConfig) {
        try {
            IpConfig saved = ipConfigService.save(ipConfig);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<IpConfig> update(@PathVariable Long id, @RequestBody IpConfig ipConfig) {
        ipConfig.setId(id);
        IpConfig updated = ipConfigService.save(ipConfig);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ipConfigService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}