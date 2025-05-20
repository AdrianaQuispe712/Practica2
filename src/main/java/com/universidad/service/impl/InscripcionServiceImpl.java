package com.universidad.service.impl;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Inscripcion;
import com.universidad.registro.model.Usuario;
import com.universidad.model.Materia;
import com.universidad.repository.InscripcionRepository;
import com.universidad.registro.repository.UsuarioRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IInscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionServiceImpl implements IInscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Override
    public List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId) {
        return inscripcionRepository.findAll().stream()
                .filter(inscripcion -> inscripcion.getEstudiante().getId().equals(estudianteId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InscripcionDTO crearInscripcion(InscripcionDTO inscripcionDTO) {
        Usuario estudiante = usuarioRepository.findById(inscripcionDTO.getEstudianteId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        Materia materia = materiaRepository.findById(inscripcionDTO.getMateriaId())
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudiante(estudiante);
        inscripcion.setMateria(materia);
        inscripcion.setEstado(inscripcionDTO.getEstado());

        Inscripcion savedInscripcion = inscripcionRepository.save(inscripcion);
        return convertToDTO(savedInscripcion);
    }

    @Override
    public InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO inscripcionDTO) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        inscripcion.setEstado(inscripcionDTO.getEstado());
        Inscripcion updatedInscripcion = inscripcionRepository.save(inscripcion);
        return convertToDTO(updatedInscripcion);
    }

    @Override
    public void eliminarInscripcion(Long id) {
        inscripcionRepository.deleteById(id);
    }

    private InscripcionDTO convertToDTO(Inscripcion inscripcion) {
        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .estudianteId(inscripcion.getEstudiante().getId())
                .materiaId(inscripcion.getMateria().getId())
                .estado(inscripcion.getEstado())
                .build();
    }
}
