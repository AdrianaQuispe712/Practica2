package com.universidad.validation;

import java.time.LocalDateTime;

import lombok.*;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private int status;
    private String mensaje;
    private Object detalles;
    private LocalDateTime timestamp;
}