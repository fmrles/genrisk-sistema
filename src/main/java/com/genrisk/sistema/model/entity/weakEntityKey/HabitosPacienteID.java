package com.genrisk.sistema.model.entity.weakEntityKey;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable

public class HabitosPacienteID implements Serializable {
    @Column(name = "itemformu", nullable = false)
    private Integer itemFormu;

    @Column(name = "formulario_id", nullable = false)
    private Integer formularioId;
}
