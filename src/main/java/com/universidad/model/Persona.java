package com.universidad.model;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter // Genera un getter para todos los campos de la clase
@Setter // Genera un setter para todos los campos de la clase
//@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity // Anotación que indica que esta clase es una entidad JPA
@Table(name = "persona") // Nombre de la tabla en la base de datos
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia de herencia para JPA
public abstract class Persona {
    // Atributos de la clase Persona

    @Id // Anotación que indica que este campo es la clave primaria
    @Column(name = "id_persona") // Nombre de la columna en la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private Long id;

    @Version
    private Long version; // Campo para manejar la versión de la entidad, útil para el control de concurrencia

    @Column(nullable = false, length = 50) // Columna no nula con longitud máxima de 50 caracteres
    // El nombre de la persona no puede ser nulo y tiene una longitud máxima de 50 caracteres
    @Basic(optional = false) // Columna no nula
    @Length(min = 3, max = 50) // Validación de longitud
    private String nombre;

    @Column(nullable = false, length = 50) // Columna no nula con longitud máxima de 50 caracteres
    @Length(min = 3, max = 50) // Validación de longitud
    // El apellido de la persona no puede ser nulo y tiene una longitud máxima de 50 caracteres
    @Basic(optional = false) // Columna no nula
    private String apellido;

    @Column(nullable = false, unique = true) // Columna no nula y con valor único
    @Basic(optional = false) // Columna no nula
    private String email;

    @Column(name = "fecha_nacimiento", nullable = false) // Columna no nula con nombre personalizado
    @Temporal(TemporalType.DATE) // Tipo de dato fecha
    @Basic(optional = false) // Columna no nula
    private LocalDate fechaNacimiento;
}