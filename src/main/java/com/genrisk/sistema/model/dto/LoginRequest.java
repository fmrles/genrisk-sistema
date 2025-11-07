package com.genrisk.sistema.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String clave;
}