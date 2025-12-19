package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Formulario;
import com.genrisk.sistema.model.entity.Histopatologia;
import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.model.entity.weakEntityKey.HistopatologiaID;
import com.genrisk.sistema.repository.FormularioRepository;
import com.genrisk.sistema.repository.HistopatologiaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistopatologiaServiceTest {

    @Mock
    private HistopatologiaRepository histopatologiaRepository;

    @Mock
    private FormularioRepository formularioRepository;

    @InjectMocks
    private HistopatologiaService histopatologiaService;

    @Test
    void cuandoPacienteEsCasoDebePermitirCrearHistopatologia() {
        // Given
        HistopatologiaID id = new HistopatologiaID(1, 100);
        Histopatologia histo = new Histopatologia();
        histo.setHistoID(id);
        histo.setTipo("Intestinal");

        Formulario formulario = new Formulario();
        formulario.setIdFormulario(100);

        Paciente pacienteCaso = new Paciente();
        pacienteCaso.setTipoPaciente("Caso");
        formulario.setPaciente(pacienteCaso);

        when(formularioRepository.findById(100)).thenReturn(Optional.of(formulario));
        when(histopatologiaRepository.save(any(Histopatologia.class))).thenReturn(histo);

        ResponseEntity<Histopatologia> response = histopatologiaService.createPost(histo);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getTipo()).isEqualTo("Intestinal");
    }

    @Test
    void cuandoPacienteEsControlDebeLanzarExcepcion() {
        // Given
        HistopatologiaID id = new HistopatologiaID(1, 200);
        Histopatologia histo = new Histopatologia();
        histo.setHistoID(id);

        Formulario formulario = new Formulario();
        formulario.setIdFormulario(200);

        Paciente pacienteControl = new Paciente();
        pacienteControl.setTipoPaciente("Control");
        formulario.setPaciente(pacienteControl);

        when(formularioRepository.findById(200)).thenReturn(Optional.of(formulario));

        
        assertThatThrownBy(() -> histopatologiaService.createPost(histo))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No está permitido registrar Histopatología")
                .hasMessageContaining("grupo 'Control'");
    }
}