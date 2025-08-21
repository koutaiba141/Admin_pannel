package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IpConfigService {
    @Autowired
    private IpConfigRepository ipConfigRepository;

    public List<IpConfig> findAll() {
        return ipConfigRepository.findAll();
    }

    public List<String> getAllowedIps() {
        return ipConfigRepository.findAllActiveIpAddresses();
    }

    public IpConfig save(IpConfig ipConfig) {
        return ipConfigRepository.save(ipConfig);
    }

    public void deleteById(Long id) {
        ipConfigRepository.deleteById(id);
    }
}