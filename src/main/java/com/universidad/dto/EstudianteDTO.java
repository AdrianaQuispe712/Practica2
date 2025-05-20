package com.universidad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.io.Serializable;

import jakarta.validation.constraints.*;

/**
 * DTO que representa los datos de un estudiante para transferencia entre capas.
 * Incluye validaciones para los campos principales y datos de inscripción, estado y fechas.
 *
 * @author Universidad
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteDTO implements Serializable {
    /** Identificador único del estudiante */
    private Long id;
    /** Nombre del estudiante */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    /** Apellido del estudiante */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
    private String apellido;
    /** Email del estudiante */
    @NotBlank( message = "El email no puede estar vacío")
    @Email( message = "El email no es válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;
    /** Fecha de nacimiento del estudiante */
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private LocalDate fechaNacimiento;
    /** Número de inscripción único del estudiante */
    @NotBlank(message = "El número de inscripción es obligatorio")
    @Size(min = 5, max = 20, message = "El número de inscripción debe tener entre 5 y 20 caracteres")
    private String numeroInscripcion;
    /** Estado actual del estudiante (activo o inactivo) */
    @NotBlank(message = "El estado es obligatorio")
    @Size(min = 3, max = 20, message = "El estado debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^(activo|inactivo)$", message = "El estado debe ser 'activo' o 'inactivo'")
    private String estado;
    /** Usuario que dio de alta al estudiante */
    @NotBlank(message = "El usuario de alta es obligatorio")
    @Size(min = 3, max = 50, message = "El usuario de alta debe tener entre 3 y 50 caracteres")
    private String usuarioAlta;
    /** Fecha de alta del estudiante */
    @NotNull(message = "La fecha de alta es obligatoria")
    @PastOrPresent(message = "La fecha de alta debe ser anterior o igual a la fecha actual")
    private LocalDate fechaAlta;
    /** Usuario que modificó al estudiante */
    @Size(min = 3, max = 50, message = "El usuario de modificacion debe tener entre 3 y 50 caracteres")
    private String usuarioModificacion;
    /** Fecha de modificación del estudiante */
    @FutureOrPresent(message = "La fecha de modificacion debe ser mayor o igual a la fecha actual")
    private LocalDate fechaModificacion;
    /** Usuario que dio de baja al estudiante */
    @Size(min = 3, max = 50, message = "El usuario de baja debe tener entre 3 y 50 caracteres")
    private String usuarioBaja;
    /** Fecha de baja del estudiante */
    @FutureOrPresent(message = "La fecha de baja debe ser mayor o igual a la fecha actual")
    private LocalDate fechaBaja;
    /** Motivo de baja del estudiante (renuncia, desercion o traslado) */
    @Size(min = 3, max = 100, message = "El motivo de baja debe tener entre 3 y 100 caracteres")
    @Pattern(regexp = "^(renuncia|desercion|traslado)$", message = "El motivo de baja debe ser 'renuncia', 'desercion' o 'traslado'")
    private String motivoBaja;
    
}