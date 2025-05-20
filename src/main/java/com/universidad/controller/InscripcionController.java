package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.service.IInscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    @Autowired
    private IInscripcionService inscripcionService;

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorEstudiante(@PathVariable Long estudianteId) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorEstudiante(estudianteId);
        return ResponseEntity.ok(inscripciones);
    }

    @PostMapping
    public ResponseEntity<InscripcionDTO> crearInscripcion(@RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO nuevaInscripcion = inscripcionService.crearInscripcion(inscripcionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaInscripcion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InscripcionDTO> actualizarInscripcion(@PathVariable Long id, @RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO inscripcionActualizada = inscripcionService.actualizarInscripcion(id, inscripcionDTO);
        return ResponseEntity.ok(inscripcionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}
