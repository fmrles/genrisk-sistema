package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.DatosGenerales;
import com.genrisk.sistema.model.entity.weakEntityKey.DatosGeneralesID;
import com.genrisk.sistema.repository.DatosGeneralesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DatosGeneralesServiceTest {

    @Mock 
    private DatosGeneralesRepository datosGeneralesRepository;

    @InjectMocks
    private DatosGeneralesService datosGeneralesService;

    @Test
    public void cuandoCreaDatosGenerales_debeRetornar201() {
        DatosGeneralesID id = new DatosGeneralesID(1, 1);
        DatosGenerales datos = new DatosGenerales();
        datos.setIdDatosGen(id);
        datos.setEdad(45);
        datos.setSexo("Masculino");

        when(datosGeneralesRepository.save(any(DatosGenerales.class))).thenReturn(datos);

        ResponseEntity<DatosGenerales> response = datosGeneralesService.createPost(datos);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getEdad()).isEqualTo(45);
    }

    @Test
    public void cuandoEliminaDatosGenerales_debeRetornar204() {
        DatosGeneralesID id = new DatosGeneralesID(1, 1);

        when(datosGeneralesRepository.findById(id)).thenReturn(Optional.of(new DatosGenerales()));

        ResponseEntity<Void> response = datosGeneralesService.deleteById(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(datosGeneralesRepository).deleteById(id);
    }
    
}
