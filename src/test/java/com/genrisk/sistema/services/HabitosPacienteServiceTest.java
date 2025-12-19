package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.HabitosPaciente;
import com.genrisk.sistema.model.entity.weakEntityKey.HabitosPacienteID;
import com.genrisk.sistema.repository.HabitosPacienteRepository;
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
public class HabitosPacienteServiceTest {

    @Mock
    private HabitosPacienteRepository habitosPacienteRepository;

    @InjectMocks
    private HabitosPacienteService habitosPacienteService;

    

    @Test
    void cuandoActualizaHabitosPaciente_debeRetornarDatosModificados() {
       
        HabitosPacienteID id = new HabitosPacienteID(1, 60);

        HabitosPaciente habitosExistente = new HabitosPaciente();
        habitosExistente.setIdHabPaciente(id);
        habitosExistente.setEstadoConsumoTabaco("Fumador actual");
        habitosExistente.setEstadoConsumoAlcohol("Sí");

        HabitosPaciente habitosActualizados = new HabitosPaciente();
        habitosActualizados.setEstadoConsumoTabaco("Ex fumador");
        habitosActualizados.setEstadoConsumoAlcohol("No");  

        // Se encuentra el existente y guarda el actualizado
        when(habitosPacienteRepository.findById(id))
                .thenReturn(Optional.of(habitosExistente));

        when(habitosPacienteRepository.save(any(HabitosPaciente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));  // Devuelve lo que se guardó

       
        PutCommand<HabitosPaciente, HabitosPacienteID> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(habitosActualizados);

        ResponseEntity<HabitosPaciente> response = habitosPacienteService.updatePut(command);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEstadoConsumoTabaco()).isEqualTo("Ex fumador");
        assertThat(response.getBody().getEstadoConsumoAlcohol()).isEqualTo("No");
    }

    @Test
    void cuandoEliminaHabitosPaciente_debeRetornar204() {
       
        HabitosPacienteID id = new HabitosPacienteID(1, 70);
        when(habitosPacienteRepository.findById(id)).thenReturn(Optional.of(new HabitosPaciente()));

        ResponseEntity<Void> response = habitosPacienteService.deleteById(id);
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(habitosPacienteRepository).deleteById(id);
    }
}