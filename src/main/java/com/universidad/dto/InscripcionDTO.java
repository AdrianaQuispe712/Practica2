package com.universidad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionDTO {
    private Long id;
    private Long estudianteId;
    private Long materiaId;
    private String estado; // Puede ser "inscrito", "cancelado", etc.
}
