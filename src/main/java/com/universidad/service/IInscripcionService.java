package com.universidad.service;

import com.universidad.dto.InscripcionDTO;

import java.util.List;

public interface IInscripcionService {
    List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId);
    InscripcionDTO crearInscripcion(InscripcionDTO inscripcionDTO);
    InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO inscripcionDTO);
    void eliminarInscripcion(Long id);
}
