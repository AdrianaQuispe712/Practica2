package com.universidad.model;

import com.universidad.registro.model.Usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inscripcion")
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante; // Asumiendo que el estudiante es un Usuario
    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;
    @Column(nullable = false)
    private String estado; // Puede ser "inscrito", "cancelado", etc.
    public void setEstudiante(Usuario estudiante2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEstudiante'");
    }
    public void setMateria(Materia materia2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMateria'");
    }
    public void setEstado(String estado2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEstado'");
    }
}