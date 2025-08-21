# Security Setup Guide

## ⚠️ IMPORTANT: Before Pushing to GitHub

This project contains sensitive configuration that must be secured before sharing publicly.

### ✅ Already Secured:
- JWT keys are dynamically generated (not hardcoded)
- Passwords are BCrypt hashed
- CORS is properly configured

### 🔧 Setup Required:

1. **Set Environment Variables:**
   ```bash
   export DB_USERNAME=your_postgres_username
   export DB_PASSWORD=your_postgres_password
   export DB_NAME=your_database_name
   ```

2. **Or Create Local Config:**
   Create `application-local.properties` in `/src/main/resources/`:
   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=your_actual_password
   spring.datasource.url=jdbc:postgresql://localhost:5432/springuser
   ```

3. **Frontend Environment:**
   Copy `.env.example` to `.env` in `/admin/admin_page/`

### 🚫 Files That Are Now Ignored:
- `application-local.properties`
- `.env` files
- Any `application-*.properties` files

### ✅ Safe to Commit:
- `application.properties` (uses environment variables)
- `application.properties.example`
- `.env.example`

## 🔒 Production Deployment Notes:
- Use proper secret management (AWS Secrets Manager, Azure Key Vault, etc.)
- Enable HTTPS in production
- Configure proper CORS origins (not `*`)
- Set strong database passwords
- Use environment-specific configuration files
