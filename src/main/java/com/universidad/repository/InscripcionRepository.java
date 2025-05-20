package com.universidad.repository;
import com.universidad.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    // Puedes agregar métodos personalizados si lo necesitas
}