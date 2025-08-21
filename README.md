Full-Stack Employee Management System Description
🎯 Project Overview
This is a comprehensive full-stack web application built with Spring Boot (Backend) and React (Frontend) for managing organizational structure, employees, roles, and security. It's designed as an enterprise-grade admin dashboard with sophisticated authentication and authorization.

🏗️ Architecture Breakdown
Backend (Spring Boot 3.5.3)
Framework: Spring Boot with Spring Security
Database: PostgreSQL (configurable for MySQL/H2)
Authentication: JWT-based stateless authentication
API Style: RESTful with standardized endpoints
Frontend (React 18)
UI Framework: Material-UI (MUI) v5 with custom theming
Routing: React Router v6 with protected routes
State Management: React Context for authentication
Data Grid: MUI X DataGrid for advanced table functionality
HTTP Client: Axios with interceptors for JWT handling
🔐 Role Management Implementation
Backend Implementation:
1. Model (Entity) - Role.java

@Entity
public class Role {
    @Id @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String role;        // Role name (ADMIN, USER, MANAGER)
    @Column(nullable = false) 
    private Long userId;        // Links role to specific user
}
2. Repository - RoleRepository.java

public interface RoleRepository extends JpaRepository<Role, Long> {}
Extends JpaRepository for automatic CRUD operations
3. Service - RoleService.java

@Service
public class RoleService {
    @Autowired private RoleRepository roleRepository;
    
    public List<Role> findAll()
    public Optional<Role> findById(Long id)
    public Role save(Role role)
    public void deleteById(Long id)
}
4. Controller - RoleController.java

@RestController
@RequestMapping("/roles")
public class RoleController {
    GET /roles           // Get all roles
    GET /roles/{id}      // Get role by ID
    POST /roles          // Create new role
    PUT /roles/{id}      // Update existing role
    DELETE /roles/{id}   // Delete role
}
Frontend Implementation:
1. API Service - api.js

export const rolesAPI = {
  getAll: () => api.get('/roles'),
  create: (role) => api.post('/roles', role),
  update: (id, role) => api.put(`/roles/${id}`, role),
  delete: (id) => api.delete(`/roles/${id}`)
};
2. React Component - RolesManagement.js

const RolesManagement = () => {
  const [roles, setRoles] = useState([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  
  // CRUD Operations:
  const fetchRoles = async () => { /* GET all roles */ }
  const handleSubmit = async () => { /* POST/PUT role */ }
  const handleDelete = async (id) => { /* DELETE role */ }
  
  // UI: MUI Table with Add/Edit/Delete buttons
};
🌟 Complete System Features
Core Modules:
Authentication System

JWT token-based authentication
BCrypt password hashing
Automatic token refresh and logout on expiry
Employee Management

Full CRUD operations with team assignments
Integration with organizational hierarchy
Advanced data grid with filtering/sorting
Organizational Hierarchy

Multi-level structure (ROOT → BRANCH → DEPARTMENT → TEAM)
Tree visualization and management
Parent-child relationship handling
Role & Permission System

User role assignments
Granular permission management
Role-based access control
IP Access Control

IP whitelist management
Rate limiting per IP address
Active/inactive IP configuration
Frontend Features:
Responsive Design: Material-UI with dark/light theme toggle
Protected Routes: Authentication-based route protection
Real-time Updates: Automatic data refresh after operations
Error Handling: Comprehensive error handling with user feedback
Professional UI: Modern dashboard with consistent styling
Security Features:
JWT Authentication: Stateless backend authentication
CORS Configuration: Secure cross-origin resource sharing
Input Validation: Both frontend and backend validation
Rate Limiting: IP-based connection rate limiting
Secure Storage: Local storage for JWT tokens with automatic cleanup
