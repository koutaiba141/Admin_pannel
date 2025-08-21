package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private HierarchyRepository hierarchyRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findByActiveTrue();
    }

    public List<Employee> getEmployeesByTeam(Long teamId) {
        return employeeRepository.findByTeamIdAndActiveTrue(teamId);
    }

    public List<Employee> getAllEmployeesInTeams() {
        return employeeRepository.findAllActiveEmployeesInTeams();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        // Validate that the team exists and is a TEAM level
        if (employee.getTeamId() != null) {
            Optional<Hierarchy> team = hierarchyRepository.findById(employee.getTeamId());
            if (team.isEmpty()) {
                throw new IllegalArgumentException("Team not found with ID: " + employee.getTeamId());
            }
            if (!"TEAM".equals(team.get().getLevel())) {
                throw new IllegalArgumentException("Employee can only be assigned to TEAM level hierarchy");
            }
            if (!team.get().getActive()) {
                throw new IllegalArgumentException("Cannot assign employee to inactive team");
            }
            employee.setTeam(team.get());
        } else {
            throw new IllegalArgumentException("Employee must be assigned to a team");
        }

        // Validate email uniqueness
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + employee.getEmail());
        }

        employee.setActive(true);
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isEmpty()) {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }

        Employee employee = existingEmployee.get();

        // Validate that the team exists and is a TEAM level
        if (employeeDetails.getTeamId() != null) {
            Optional<Hierarchy> team = hierarchyRepository.findById(employeeDetails.getTeamId());
            if (team.isEmpty()) {
                throw new IllegalArgumentException("Team not found with ID: " + employeeDetails.getTeamId());
            }
            if (!"TEAM".equals(team.get().getLevel())) {
                throw new IllegalArgumentException("Employee can only be assigned to TEAM level hierarchy");
            }
            if (!team.get().getActive()) {
                throw new IllegalArgumentException("Cannot assign employee to inactive team");
            }
            employee.setTeam(team.get());
        } else {
            throw new IllegalArgumentException("Employee must be assigned to a team");
        }

        // Validate email uniqueness (excluding current employee)
        if (employeeRepository.existsByEmailAndIdNot(employeeDetails.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists: " + employeeDetails.getEmail());
        }

        // Update fields
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhone(employeeDetails.getPhone());
        employee.setPosition(employeeDetails.getPosition());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setHireDate(employeeDetails.getHireDate());
        employee.setSalary(employeeDetails.getSalary());
        employee.setNotes(employeeDetails.getNotes());

        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }

        // Soft delete by setting active to false
        Employee emp = employee.get();
        emp.setActive(false);
        employeeRepository.save(emp);
    }

    public boolean employeeExists(Long id) {
        return employeeRepository.existsById(id);
    }
} 