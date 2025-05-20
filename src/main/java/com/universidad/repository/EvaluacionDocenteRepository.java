package com.universidad.repository;

import com.universidad.model.EvaluacionDocente;
import com.universidad.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluacionDocenteRepository extends JpaRepository<EvaluacionDocente, Long> {
    List<EvaluacionDocente> findByDocente(Docente docente);
}
