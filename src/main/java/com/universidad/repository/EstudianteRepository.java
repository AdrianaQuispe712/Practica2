package com.universidad.repository; // Define el paquete al que pertenece esta clase

import com.universidad.model.Estudiante; // Importa la clase Estudiante del paquete model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Importa la anotación Repository de Spring
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository // Anotación que indica que esta clase es un repositorio de Spring
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    // No es necesario implementar métodos básicos como findAll, ya que JpaRepository los proporciona automáticamente.

    Boolean existsByEmail(String email); // Método para verificar si existe un estudiante por su correo electrónico
    Boolean existsByNumeroInscripcion(String numeroInscripcion); // Método para verificar si existe un estudiante por su número de inscripción

    // Método para encontrar un estudiante por su número de inscripción
    Estudiante findByNumeroInscripcion(String numeroInscripcion); 

    // Método para encontrar un estudiante por su estado
    Estudiante findByEstado(String estado); // Método para encontrar un estudiante por su estado

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Estudiante> findById(Long id); // Método para encontrar un estudiante por su ID con bloqueo pesimista
    // Este método se utiliza para evitar condiciones de carrera al actualizar el estudiante
    

}