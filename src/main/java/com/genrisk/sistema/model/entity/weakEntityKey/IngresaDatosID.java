package com.genrisk.sistema.model.entity.weakEntityKey;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable

public class IngresaDatosID implements Serializable {
    @Column(name = "id_ingreso", nullable = false)
    private Integer idIngreso;

    @Column(name = "formulario_id", nullable = false)
    private Integer formularioId;
}