package com.universidad.service.impl; // Define el paquete al que pertenece esta clase

import com.universidad.dto.EstudianteDTO; // Importa la clase EstudianteDTO del paquete dto
import com.universidad.model.Estudiante; // Importa la clase Estudiante del paquete model
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository; // Importa la clase EstudianteRepository del paquete repository
import com.universidad.service.IEstudianteService; // Importa la interfaz IEstudianteService del paquete service
import com.universidad.validation.EstudianteValidator; // Importa la clase EstudianteValidator del paquete validation

import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación Autowired de Spring
import org.springframework.stereotype.Service; // Importa la anotación Service de Spring
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List; // Importa la interfaz List para manejar listas
import java.util.stream.Collectors; // Importa la clase Collectors para manejar colecciones

@Service // Anotación que indica que esta clase es un servicio de Spring
public class EstudianteServiceImpl implements IEstudianteService { // Define la clase EstudianteServiceImpl que implementa la interfaz IEstudianteService

    @Autowired
    private EstudianteRepository estudianteRepository; // Inyección de dependencias del repositorio de estudiantes

    @Autowired // Inyección de dependencias del validador de estudiantes
    private EstudianteValidator estudianteValidator; // Declara una variable para el validador de estudiantes
    
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, EstudianteValidator estudianteValidator) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteValidator = estudianteValidator;
    }

    /*public EstudianteServiceImpl(EstudianteRepository estudianteRepository) {
            this.estudianteRepository = estudianteRepository;
    }*/

    @Override
    @Cacheable(value = "estudiantes")
    public List<EstudianteDTO> obtenerTodosLosEstudiantes() {
        // Obtiene todos los estudiantes y los convierte a DTO
        return estudianteRepository.findAll().stream() // Obtiene todos los estudiantes de la base de datos
                .map(this::convertToDTO) // Convierte cada Estudiante a EstudianteDTO
                .collect(Collectors.toList()); // Recoge los resultados en una lista
    }

    @Override
    @Cacheable(value = "estudiante", key = "#numeroInscripcion")
    public EstudianteDTO obtenerEstudiantePorNumeroInscripcion(String numeroInscripcion) {
        // Busca un estudiante por su número de inscripción y lo convierte a DTO
        Estudiante estudiante = estudianteRepository.findByNumeroInscripcion(numeroInscripcion); // Busca el estudiante por su número de inscripción
        return convertToDTO(estudiante); // Convierte el Estudiante a EstudianteDTO y lo retorna
    }

    @Override
    @Cacheable(value = "estudiantesActivos")
    public List<EstudianteDTO> obtenerEstudianteActivo() { // Método para obtener una lista de estudiantes activos
        // Busca todos los estudiantes activos y los convierte a DTO
        return estudianteRepository.findAll().stream() // Obtiene todos los estudiantes de la base de datos
                .filter(estudiante -> "activo".equalsIgnoreCase(estudiante.getEstado())) // Filtra los estudiantes activos
                .map(this::convertToDTO) // Convierte cada Estudiante a EstudianteDTO
                .collect(Collectors.toList()); // Recoge los resultados en una lista
    }


    @Override
    @Cacheable(value = "materiasEstudiante", key = "#estudianteId")
    public List<Materia> obtenerMateriasDeEstudiante(Long estudianteId) { // Método para obtener las materias de un estudiante por su ID
        // Busca el estudiante por su ID y obtiene sus materias
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        return estudiante.getMaterias();
    }

    @Override
    @CachePut(value = "estudiante", key = "#result.numeroInscripcion")
    @CacheEvict(value = {"estudiantes", "estudiantesActivos"}, allEntries = true)
    public EstudianteDTO crearEstudiante(EstudianteDTO estudianteDTO) { // Método para crear un nuevo estudiante
        
        estudianteValidator.validacionCompletaEstudiante(estudianteDTO); // Valida el estudiante usando el validador

        // Convierte el DTO a entidad, guarda el estudiante y lo convierte de nuevo a DTO
        Estudiante estudiante = convertToEntity(estudianteDTO); // Convierte el EstudianteDTO a Estudiante
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante); // Guarda el estudiante en la base de datos
        return convertToDTO(estudianteGuardado); // Convierte el Estudiante guardado a EstudianteDTO y lo retorna
    }

    @Override
    @CachePut(value = "estudiante", key = "#id")
    @CacheEvict(value = {"estudiantes", "estudiantesActivos"}, allEntries = true)
    public EstudianteDTO actualizarEstudiante(Long id, EstudianteDTO estudianteDTO) { // Método para actualizar un estudiante existente
        // Busca el estudiante por su ID, actualiza sus datos y lo guarda de nuevo
        Estudiante estudianteExistente = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado")); // Lanza una excepción si el estudiante no se encuentra
        estudianteExistente.setNombre(estudianteDTO.getNombre()); // Actualiza el nombre
        estudianteExistente.setApellido(estudianteDTO.getApellido()); // Actualiza el apellido
        estudianteExistente.setEmail(estudianteDTO.getEmail()); // Actualiza el email
        estudianteExistente.setFechaNacimiento(estudianteDTO.getFechaNacimiento()); // Actualiza la fecha de nacimiento
        estudianteExistente.setNumeroInscripcion(estudianteDTO.getNumeroInscripcion()); // Actualiza el número de inscripción
        estudianteExistente.setUsuarioModificacion("admin"); // Actualiza el usuario de modificación
        estudianteExistente.setFechaModificacion(LocalDate.now()); // Actualiza la fecha de modificación

        Estudiante estudianteActualizado = estudianteRepository.save(estudianteExistente); // Guarda el estudiante actualizado en la base de datos
        return convertToDTO(estudianteActualizado); // Convierte el Estudiante actualizado a EstudianteDTO y lo retorna
    }

    @Override
    @CacheEvict(value = {"estudiante", "estudiantes", "estudiantesActivos"}, allEntries = true)
    public EstudianteDTO eliminarEstudiante(Long id, EstudianteDTO estudianteDTO) { // Método para eliminar (de manera lógica) un estudiante por su ID
        Estudiante estudianteExistente = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado")); // Lanza una excepción si el estudiante no se encuentra
        estudianteExistente.setEstado("inactivo"); // Actualiza el estado a inactivo
        estudianteExistente.setUsuarioBaja("admin"); // Asigna el usuario que dio de baja al estudiante
        estudianteExistente.setFechaBaja(LocalDate.now()); // Actualiza la fecha de baja
        estudianteExistente.setMotivoBaja(estudianteDTO.getMotivoBaja()); // Actualiza el motivo de baja

        Estudiante estudianteInactivo = estudianteRepository.save(estudianteExistente); // Guarda el estudiante inactivo en la base de datos
        return convertToDTO(estudianteInactivo); // Convierte el Estudiante inactivo a EstudianteDTO y lo retorna
    }

    @Transactional
    public Estudiante obtenerEstudianteConBloqueo(Long id) {
        Estudiante est = estudianteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        // Simula un tiempo de procesamiento prolongado
        // Esto es solo para demostrar el bloqueo, en un caso real no se debería hacer esto
            try { Thread.sleep(15000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        // Simula un tiempo de procesamiento prolongado
        return est;
    }

    // Método auxiliar para convertir entidad a DTO
    private EstudianteDTO convertToDTO(Estudiante estudiante) { // Método para convertir un Estudiante a EstudianteDTO
        return EstudianteDTO.builder() // Usa el patrón builder para crear un EstudianteDTO
                .id(estudiante.getId()) // Asigna el ID
                .nombre(estudiante.getNombre()) // Asigna el nombre
                .apellido(estudiante.getApellido()) // Asigna el apellido
                .email(estudiante.getEmail()) // Asigna el email
                .fechaNacimiento(estudiante.getFechaNacimiento()) // Asigna la fecha de nacimiento
                .numeroInscripcion(estudiante.getNumeroInscripcion()) // Asigna el número de inscripción
                .estado(estudiante.getEstado()) // Asigna el estado (puede ser null si no se desea mostrar)
                .usuarioAlta(estudiante.getUsuarioAlta()) // Asigna el usuario de alta
                .fechaAlta(estudiante.getFechaAlta()) // Asigna la fecha de alta (puede ser null si no se desea mostrar)
                .usuarioModificacion(estudiante.getUsuarioModificacion()) // Asigna el usuario de modificación
                .usuarioBaja(estudiante.getUsuarioBaja()) // Asigna el usuario de baja (puede ser null si no se desea mostrar)
                .fechaBaja(estudiante.getFechaBaja()) // Asigna la fecha de baja (puede ser null si no se desea mostrar)
                .motivoBaja(estudiante.getMotivoBaja()) // Asigna el motivo de baja (puede ser null si no se desea mostrar)
                .build(); // Construye el objeto EstudianteDTO
    }
    
    // Método auxiliar para convertir DTO a entidad
    private Estudiante convertToEntity(EstudianteDTO estudianteDTO) { // Método para convertir un EstudianteDTO a Estudiante
        return Estudiante.builder() // Usa el patrón builder para crear un Estudiante
                .id(estudianteDTO.getId()) // Asigna el ID
                .nombre(estudianteDTO.getNombre()) // Asigna el nombre
                .apellido(estudianteDTO.getApellido()) // Asigna el apellido
                .email(estudianteDTO.getEmail()) // Asigna el email
                .fechaNacimiento(estudianteDTO.getFechaNacimiento()) // Asigna la fecha de nacimiento
                .numeroInscripcion(estudianteDTO.getNumeroInscripcion())  // Asigna el número de inscripción
                .usuarioAlta(estudianteDTO.getUsuarioAlta()) // Asigna el usuario de alta
                .fechaAlta(estudianteDTO.getFechaAlta()) // Asigna la fecha de alta
                .usuarioModificacion(estudianteDTO.getUsuarioModificacion()) // Asigna el usuario de modificación
                .fechaModificacion(estudianteDTO.getFechaModificacion()) // Asigna la fecha de modificación
                .estado(estudianteDTO.getEstado()) // Asigna el estado (puede ser null si no se desea mostrar)
                .usuarioBaja(estudianteDTO.getUsuarioBaja()) // Asigna el usuario de baja (puede ser null si no se desea mostrar)
                .fechaBaja(estudianteDTO.getFechaBaja()) // Asigna la fecha de baja (puede ser null si no se desea mostrar)
                .motivoBaja(estudianteDTO.getMotivoBaja()) // Asigna el motivo de baja (puede ser null si no se desea mostrar)
                .build(); // Construye el objeto Estudiante
    }
}