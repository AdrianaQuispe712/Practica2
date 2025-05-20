package com.universidad.validation;

import org.springframework.stereotype.Component;

import com.universidad.dto.EstudianteDTO;
import com.universidad.repository.EstudianteRepository; // Importa la interfaz EstudianteRepository
import java.util.Arrays;
import java.util.List;


@Component // Indica que esta clase es un componente de Spring
public class EstudianteValidator {

    private final EstudianteRepository estudianteRepository;

    public EstudianteValidator(EstudianteRepository userRepository) {
        this.estudianteRepository = userRepository;
    }

    public void validaEmailUnico(String email) {
        if (estudianteRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe un usuario con este email");
        }
    }

    public void validaDominioEmail(String email) {
        String dominio = email.substring(email.indexOf('@') + 1);
        List<String> dominiosBloqueados = Arrays.asList("dominiobloqueado.com", "spam.com");

        if (dominiosBloqueados.contains(dominio)) {
            throw new IllegalArgumentException("El dominio de email no está permitido");
        }
    }

    // Validación manual para nombre vacío o nulo
    public void validaNombreEstudiante(String nombre){
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío o nulo.");
        }
    }
  
    // Validación manual para apellido vacío o nulo
    public void validaApellidoEstudiante(String apellido){
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio y no puede estar vacío.");
        }
    }

    public void validacionCompletaEstudiante(EstudianteDTO estudiante) {
        validaEmailUnico(estudiante.getEmail());
        validaDominioEmail(estudiante.getEmail());
        validaNombreEstudiante(estudiante.getNombre());
        validaApellidoEstudiante(estudiante.getApellido());
        // Otras validaciones...
    }

    public class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}
