package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "ip_config")
public class IpConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String ipAddress;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    private String description;

    public IpConfig() {}

    public IpConfig(String ipAddress, String description) {
        this.ipAddress = ipAddress;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}