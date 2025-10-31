package com.genrisk.sistema.model.entity.weakEntityKey;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable

public class muestraID implements Serializable {
    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(name = "nro_muestra", nullable = false)
    private Integer nroMuestra;
}
