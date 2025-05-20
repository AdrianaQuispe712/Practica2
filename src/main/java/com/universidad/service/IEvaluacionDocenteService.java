package com.universidad.service;

import com.universidad.model.EvaluacionDocente;
import java.util.List;

public interface IEvaluacionDocenteService {
    EvaluacionDocente crearEvaluacion(EvaluacionDocente evaluacion);
    List<EvaluacionDocente> obtenerEvaluacionesPorDocente(Long docenteId);
    EvaluacionDocente obtenerEvaluacionPorId(Long id);
    void eliminarEvaluacion(Long id);
}
