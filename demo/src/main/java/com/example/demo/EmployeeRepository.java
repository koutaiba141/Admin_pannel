package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    List<Employee> findByActiveTrue();
    
    List<Employee> findByTeamIdAndActiveTrue(Long teamId);
    
    @Query("SELECT e FROM Employee e WHERE e.team.level = 'TEAM' AND e.active = true")
    List<Employee> findAllActiveEmployeesInTeams();
    
    @Query("SELECT e FROM Employee e WHERE e.team.id = :teamId AND e.active = true")
    List<Employee> findActiveEmployeesByTeamId(@Param("teamId") Long teamId);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndIdNot(String email, Long id);
} 