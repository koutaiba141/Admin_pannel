# Employee Management System

A comprehensive Spring Boot application for managing employees, organizational hierarchy, roles, permissions, and IP-based access control with JWT authentication.

## 🚀 Features

- **User Authentication & Authorization**: JWT-based authentication with role-based access control
- **Employee Management**: Complete CRUD operations for employee records
- **Organizational Hierarchy**: Multi-level hierarchy management (ROOT → BRANCH → DEPARTMENT → TEAM)
- **Role & Permission System**: Granular permission management for different user roles
- **IP Access Control**: Whitelist-based IP filtering and rate limiting
- **Security**: BCrypt password hashing, CORS configuration, and secure JWT token handling

## 🏗️ Architecture

### Core Entities

- **Employee**: Employee records with personal info, position, department, and team assignments
- **Hierarchy**: Organizational structure with parent-child relationships
- **UserModel**: User accounts for authentication
- **Role**: User role assignments
- **Permission**: Fine-grained permissions system
- **IpConfig**: IP address whitelist configuration

### Technology Stack

- **Framework**: Spring Boot 3.5.3
- **Database**: Multi-database support (MySQL, PostgreSQL, H2)
- **Security**: Spring Security with JWT
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Java Version**: 17

## 📁 Project Structure

```
src/main/java/com/example/demo/
├── DemoApplication.java          # Main application class
├── config/
│   ├── SecurityConfig.java       # Security configuration
│   └── WebConfig.java            # Web configuration
├── auth/
│   ├── AuthController.java       # Authentication endpoints
│   ├── AuthRequest.java          # Login request DTO
│   ├── AuthResponse.java         # Login response DTO
│   ├── JwtUtil.java              # JWT utility class
│   ├── JwtAuthenticationFilter.java # JWT filter
│   └── CustomUserDetailsService.java # User details service
├── employee/
│   ├── Employee.java             # Employee entity
│   ├── EmployeeController.java   # Employee REST endpoints
│   ├── EmployeeService.java      # Employee business logic
│   └── EmployeeRepository.java   # Employee data access
├── hierarchy/
│   ├── Hierarchy.java            # Hierarchy entity
│   ├── HierarchyController.java  # Hierarchy REST endpoints
│   ├── HierarchyService.java     # Hierarchy business logic
│   ├── HierarchyRepository.java  # Hierarchy data access
│   └── HierarchyTreeDTO.java     # Hierarchy tree DTO
├── security/
│   ├── Role.java                 # Role entity
│   ├── RoleController.java       # Role management endpoints
│   ├── Permission.java           # Permission entity
│   └── PermissionController.java # Permission management endpoints
└── ipconfig/
    ├── IpConfig.java             # IP configuration entity
    ├── IpConfigController.java   # IP management endpoints
    └── IpConnectionRateLimitFilter.java # Rate limiting filter
```

## 🔧 Build & Run

### Prerequisites
- Java 17+
- Maven 3.6+
- Database (MySQL/PostgreSQL) or use H2 for development

### Commands

```bash
# Build the project
mvn clean compile

# Run tests
mvn test

# Run the application
mvn spring-boot:run

# Build JAR
mvn clean package

# Run JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## 🔐 API Endpoints

### Authentication
- `POST /auth/login` - User login
- `POST /auth/register` - User registration

### Employee Management
- `GET /employees` - Get all employees
- `GET /employees/{id}` - Get employee by ID
- `GET /employees/team/{teamId}` - Get employees by team
- `POST /employees` - Create new employee
- `PUT /employees/{id}` - Update employee
- `DELETE /employees/{id}` - Delete employee

### Hierarchy Management
- `GET /hierarchies` - Get all hierarchy nodes
- `POST /hierarchies` - Create hierarchy node
- `PUT /hierarchies/{id}` - Update hierarchy node
- `DELETE /hierarchies/{id}` - Delete hierarchy node

### Role & Permission Management
- `GET /roles` - Get all roles
- `POST /roles` - Create role
- `GET /permissions` - Get all permissions
- `POST /permissions` - Create permission

### IP Configuration
- `GET /api/ip-config` - Get IP configurations
- `POST /api/ip-config` - Add IP configuration
- `DELETE /api/ip-config/{id}` - Remove IP configuration

## 🔒 Security Features

- **JWT Authentication**: Stateless authentication with JWT tokens
- **Password Encryption**: BCrypt hashing for secure password storage
- **CORS Configuration**: Cross-origin resource sharing setup
- **IP Whitelisting**: Access control based on IP addresses
- **Rate Limiting**: Connection rate limiting per IP
- **Role-based Authorization**: Endpoint protection based on user roles

## 🗄️ Database Configuration

### Initial Setup
1. Copy `application.properties.example` to `application.properties`
2. Set environment variables:
   ```bash
   export DB_USERNAME=your_username
   export DB_PASSWORD=your_password
   export DB_NAME=your_database_name
   ```

### Alternative: Local Configuration
Create `application-local.properties` with your actual credentials:
```properties
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.url=jdbc:postgresql://localhost:5432/springuser
```

## 🚦 Getting Started

### Backend Setup
1. Clone the repository
2. Copy `application.properties.example` to `application.properties`
3. Set database environment variables (see Database Configuration)
4. Run `mvn spring-boot:run`

### Frontend Setup
1. Navigate to `admin/admin_page/`
2. Copy `.env.example` to `.env`
3. Run `npm install`
4. Run `npm start`

### First Use
1. Register a new user at `POST /auth/register`
2. Login via the React frontend
3. Access the admin dashboard with full functionality

## 📝 Notes

- The application uses scheduled tasks (enabled via `@EnableScheduling`)
- All protected endpoints require JWT token in Authorization header
- Employee records are linked to organizational hierarchy
- IP-based access control is enforced through custom filters
- The system supports multi-level organizational structures
