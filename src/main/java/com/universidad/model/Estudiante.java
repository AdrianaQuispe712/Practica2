package com.universidad.model; // Define el paquete al que pertenece esta clase

import lombok.AllArgsConstructor; // Importa la anotación AllArgsConstructor de Lombok para generar un constructor con todos los argumentos
import lombok.Data; // Importa la anotación Data de Lombok para generar getters, setters, toString, equals y hashCode
import lombok.EqualsAndHashCode; // Importa la anotación EqualsAndHashCode de Lombok para generar métodos equals y hashCode
import lombok.Getter;
import lombok.NoArgsConstructor; // Importa la anotación NoArgsConstructor de Lombok para generar un constructor sin argumentos
import lombok.Setter;
import lombok.experimental.SuperBuilder; // Importa la anotación SuperBuilder de Lombok para generar un builder que soporta herencia

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*; // Importa las anotaciones de JPA para la persistencia de datos

@Getter // Genera un getter para todos los campos de la clase
@Setter // Genera un setter para todos los campos de la clase
//@Data // Genera getters, setters, toString, equals y hashCode
@EqualsAndHashCode(callSuper = true) // Genera métodos equals y hashCode, incluyendo los campos de la clase padre
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@SuperBuilder // Genera un builder que soporta herencia

/**
 * Entidad JPA que representa a un estudiante en el sistema universitario.
 * Hereda los atributos comunes de la clase Persona.
 * Incluye información de inscripción, estado, fechas y materias asociadas.
 *
 * @author Universidad
 */
@Entity // Anotación que indica que esta clase es una entidad JPA
@Table(name = "estudiante") // Nombre de la tabla en la base de datos
public class Estudiante extends Persona { // Define la clase Estudiante que extiende de Persona
    
    /**
     * Número de inscripción único del estudiante.
     */
    @Column(name = "numero_inscripcion", nullable = false, unique = true) // Columna no nula y con valor único
    private String numeroInscripcion; // Campo para almacenar el número de inscripción del estudiante

    /**
     * Estado actual del estudiante (activo, inactivo, etc.).
     */
    @Column(name = "estado") // Columna opcional
    private String estado; // Campo para almacenar el estado del estudiante (activo, inactivo, etc.)

    /**
     * Usuario que dio de alta al estudiante.
     */
    @Column(name = "usuario_alta") 
    private String usuarioAlta; // Campo para almacenar el usuario que dio de alta al estudiante
    
    /**
     * Fecha de alta del estudiante.
     */
    @Column(name = "fecha_alta") // Columna opcional
    @Temporal(TemporalType.DATE) // Tipo de dato fecha
    @Basic(optional = false) // Columna no nula
    private LocalDate fechaAlta; // Campo para almacenar la fecha de alta del estudiante
    
    /**
     * Usuario que modificó al estudiante.
     */
    @Column(name = "usuario_modificacion") // Columna opcional
    private String usuarioModificacion; // Campo para almacenar el usuario que modificó al estudiante

    /**
     * Fecha de modificación del estudiante.
     */
    @Column(name = "fecha_modificacion") // Columna opcional
    @Temporal(TemporalType.DATE) // Tipo de dato fecha
    @Basic(optional = true) // Columna opcional
    private LocalDate fechaModificacion; // Campo para almacenar la fecha de modificación del estudiante
    
    /**
     * Usuario que dio de baja al estudiante.
     */
    @Column(name = "usuario_baja") // Columna opcional
    private String usuarioBaja; // Campo para almacenar el usuario que dio de baja al estudiante
    
    /**
     * Fecha de baja del estudiante.
     */
    @Column(name = "fecha_baja") // Columna opcional
    @Temporal(TemporalType.DATE) // Tipo de dato fecha
    @Basic(optional = true) // Columna opcional
    private LocalDate fechaBaja; // Campo para almacenar la fecha de baja del estudiante

    /**
     * Motivo de baja del estudiante (renuncia, deserción, traslado, etc.).
     */
    @Column(name = "motivo_baja") // Columna opcional
    private String motivoBaja; // Columna opcional para almacenar el motivo de baja del estudiante

    /**
     * Lista de materias asociadas al estudiante.
     */
    @ManyToMany(fetch = FetchType.LAZY) // Relación muchos a muchos con la entidad Materia
    @JoinTable(name = "estudiante_materia", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_estudiante"), // Columna que referencia al estudiante
            inverseJoinColumns = @JoinColumn(name = "id_materia")  // Columna que referencia a la materia 
    )
    private List<Materia> materias; // Lista de materias asociadas al estudiante

    /*@Version
    private Long version; // Campo para manejar la versión de la entidad, útil para el control de concurrencia*/

}