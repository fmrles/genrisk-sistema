package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.MiembroEquipo;
import com.genrisk.sistema.repository.MiembroEquipoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MiembroEquipoServiceTest {

    @Mock
    private MiembroEquipoRepository miembroEquipoRepository;

    @InjectMocks
    private MiembroEquipoService miembroEquipoService;

    @Test
    public void cuandoCreaNuevoMiembroDebeEncriptarClaveYGuardar() {
        MiembroEquipo miembroNuevo = new MiembroEquipo();
        miembroNuevo.setCorreoMiembro("test@email.com");
        miembroNuevo.setClave("password");
        miembroNuevo.setRolMiembro("Digitador");

        MiembroEquipo miembroGuardado = new MiembroEquipo();
        miembroGuardado.setIdMiembroEquipo(1);;
        miembroGuardado.setClave("encrypted"); // Simula encriptación

        when(miembroEquipoRepository.save(any(MiembroEquipo.class))).thenReturn(miembroGuardado);

        MiembroEquipo creado = miembroEquipoService.crearNuevoMiembro(miembroNuevo);

        assertThat(creado.getIdMiembroEquipo()).isEqualTo(1);
        assertThat(creado.getClave()).isNotEqualTo("password"); // Debe estar encriptada
    }
}