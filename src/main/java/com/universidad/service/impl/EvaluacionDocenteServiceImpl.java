package com.universidad.service.impl;

import com.universidad.model.EvaluacionDocente;
import com.universidad.model.Docente;
import com.universidad.repository.EvaluacionDocenteRepository;
import com.universidad.repository.DocenteRepository;
import com.universidad.service.IEvaluacionDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluacionDocenteServiceImpl implements IEvaluacionDocenteService {
    @Autowired
    private EvaluacionDocenteRepository evaluacionDocenteRepository;
    @Autowired
    private DocenteRepository docenteRepository;

    @Override
    public EvaluacionDocente crearEvaluacion(EvaluacionDocente evaluacion) {
        return evaluacionDocenteRepository.save(evaluacion);
    }

    @Override
    public List<EvaluacionDocente> obtenerEvaluacionesPorDocente(Long docenteId) {
        Docente docente = docenteRepository.findById(docenteId).orElse(null);
        if (docente == null) return java.util.Collections.emptyList();
        return evaluacionDocenteRepository.findByDocente(docente);
    }

    @Override
    public EvaluacionDocente obtenerEvaluacionPorId(Long id) {
        return evaluacionDocenteRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarEvaluacion(Long id) {
        evaluacionDocenteRepository.deleteById(id);
    }
}
