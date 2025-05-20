# Sistema de Registro Universitario – Práctica 2

Este proyecto es la entrega de la **Práctica 2** de la materia **Desarrollo web Backend**. Desarrollé un backend utilizando **Spring Boot** que simula un sistema de registro universitario. Implementé autenticación con JWT, validaciones, manejo de errores, cache con Redis y documentación con Swagger. También desarrollé los módulos CRUD para estudiantes, materias e inscripciones.

🔗 **Repositorio público del proyecto:**  
https://github.com/AdrianaQuispe712/Practica2

---

## 🧩 Funcionalidades desarrolladas

### 🔐 1. Módulo de Autenticación
- Registro e inicio de sesión de usuarios.
- Generación y validación de **tokens JWT**.
- Asignación de roles: `ADMIN`, `ESTUDIANTE`.
- Endpoints protegidos según rol.

### 👩‍🎓 2. CRUD de Estudiantes
- Operaciones: crear, obtener, actualizar y eliminar estudiantes.
- Validaciones: nombre obligatorio, correo válido, entre otros.
- Ejemplo: `@NotBlank`, `@Email`, `@Size`.

### 📘 3. CRUD de Materias
- Gestión completa de materias.
- Asignación de docentes a cada materia.
- Validaciones para evitar duplicaciones o campos vacíos.

### 📝 4. CRUD de Inscripciones
- Registro de materias a estudiantes.
- Validaciones para no duplicar inscripciones ni permitir campos nulos.

---

## 🛠️ Tecnologías utilizadas

- ☕ **Java 17**
- 🌱 **Spring Boot**
- 🔐 **Spring Security + JWT**
- 🐘 **PostgreSQL**
- 🧠 **Redis (Cache)**
- 📑 **Swagger (Documentación de API)**
- 🧪 **JUnit + Swagger UI (para pruebas)**

---

## 📁 Estructura del proyecto

```bash
src/main/java/com/registro/
│
├── config/           # Configuraciones generales (Redis, Swagger, seguridad)
├── controller/       # Controladores REST (Endpoints)
├── dto/              # Data Transfer Objects
├── model/            # Entidades de la base de datos
├── registro/         # Lógica de autenticación y manejo de JWT
├── repository/       # Interfaces de acceso a datos (JPA)
├── service/          # Servicios con la lógica de negocio
├── validation/       # Validaciones y excepciones personalizadas
└── Practica2Application.java
