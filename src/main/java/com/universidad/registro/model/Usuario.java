package com.universidad.registro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String nombre;
    
    private String apellido;
    
    private boolean activo = true;
    
    @ManyToMany(fetch = FetchType.EAGER) // Carga los roles de forma anticipada (EAGER) para evitar problemas de LazyInitializationException
    @JoinTable(
        name = "usuario_roles", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "usuario_id"), // Columna que referencia al usuario
        inverseJoinColumns = @JoinColumn(name = "rol_id") // Columna que referencia al rol
    )
    private Set<Rol> roles = new HashSet<>();
}
