package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.HistopatologiaID;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name = "hispatologia")
public class Histopatologia {

    @EmbeddedId 
    private HistopatologiaID histoID;

    @Column(name = "tipo", nullable = true, updatable = true)
    private String tipo;

    @Column(name = "estado_clinico", nullable = true, updatable = true)
    private String estadoClinico;

    @Column(name = "locali_tumor", nullable = true, updatable = true)
    private String localiTumor;
}
