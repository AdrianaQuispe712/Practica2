package com.universidad.repository;

import com.universidad.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocenteRepository extends JpaRepository<Docente, Long> {
    // Puedes agregar métodos personalizados si lo necesitas
}
