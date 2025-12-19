package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Formulario;
import com.genrisk.sistema.model.entity.MiembroEquipo;
import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.repository.FormularioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FormularioServiceTest {

    @Mock
    private FormularioRepository formularioRepository;

    @InjectMocks
    private FormularioService formularioService;

    @Test
    public void cuandoCreaFormulario_debeRetornar201ConIdGenerado() {
        Formulario formularioNuevo = new Formulario();
        formularioNuevo.setTipoFormulario("Control");
        formularioNuevo.setEstadoFormulario("En progreso");
        formularioNuevo.setFechaFormulario(LocalDate.now());
        formularioNuevo.setPaciente(new Paciente("pac1", "Test", "test@email.com", "Dir", "Control", LocalDate.now()));
        formularioNuevo.setMiembroEquipo(new MiembroEquipo());

        Formulario formularioGuardado = new Formulario();
        formularioGuardado.setIdFormulario(1);
        formularioGuardado.setTipoFormulario("Control");

        when(formularioRepository.save(any(Formulario.class))).thenReturn(formularioGuardado);

        ResponseEntity<Formulario> response = formularioService.createPost(formularioNuevo);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getIdFormulario()).isEqualTo(1);
    } 

    @Test
    public void cuandoActualizaFormulario_debeRetornarFormularioActualizado() {
        Integer id = 1;
        Formulario formularioExistente = new Formulario();
        formularioExistente.setIdFormulario(id);
        formularioExistente.setEstadoFormulario("En progreso");

        Formulario formularioActualizado = new Formulario();
        formularioActualizado.setEstadoFormulario("Completado");

        PutCommand<Formulario, Integer> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(formularioActualizado);

        when(formularioRepository.findById(id)).thenReturn(Optional.of(formularioExistente));
        when(formularioRepository.save(any(Formulario.class))).thenReturn(formularioExistente);

        ResponseEntity<Formulario> response = formularioService.updatePut(command);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getEstadoFormulario()).isEqualTo("Completado");
    }
}
