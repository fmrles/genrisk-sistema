package com.genrisk.sistema.model.entity;
import com.genrisk.sistema.model.entity.weakEntityKey.muestraID;


import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table (name = "muestra")
public class Muestra {

    @EmbeddedId 
    private muestraID id;

    @Column(name = "tipo_muestra", nullable = false, updatable = false)
    private String tipoMuestra;

    @Column(name = "fecha_muestra", nullable = true, updatable = true)
    private LocalDate fechaMuestra;

    @Column(name = "estado_muestra", nullable = true, updatable = true)
    private String estadoMuestra;
    
}
