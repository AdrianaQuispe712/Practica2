package com.universidad.controller;

import com.universidad.model.EvaluacionDocente;
import com.universidad.service.IEvaluacionDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones-docente")
public class EvaluacionDocenteController {
    @Autowired
    private IEvaluacionDocenteService evaluacionDocenteService;

    @PostMapping
    public ResponseEntity<EvaluacionDocente> crearEvaluacion(@RequestBody EvaluacionDocente evaluacion) {
        EvaluacionDocente nueva = evaluacionDocenteService.crearEvaluacion(evaluacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<EvaluacionDocente>> obtenerEvaluacionesPorDocente(@PathVariable Long docenteId) {
        List<EvaluacionDocente> evaluaciones = evaluacionDocenteService.obtenerEvaluacionesPorDocente(docenteId);
        return ResponseEntity.ok(evaluaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDocente> obtenerEvaluacionPorId(@PathVariable Long id) {
        EvaluacionDocente evaluacion = evaluacionDocenteService.obtenerEvaluacionPorId(id);
        if (evaluacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(evaluacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvaluacion(@PathVariable Long id) {
        evaluacionDocenteService.eliminarEvaluacion(id);
        return ResponseEntity.noContent().build();
    }
}
