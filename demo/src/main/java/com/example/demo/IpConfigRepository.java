package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface IpConfigRepository extends JpaRepository<IpConfig, Long> {
    @Query("SELECT i.ipAddress FROM IpConfig i WHERE i.active = true")
    List<String> findAllActiveIpAddresses();
}