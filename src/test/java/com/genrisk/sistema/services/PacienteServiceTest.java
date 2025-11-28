package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) 
public class PacienteServiceTest { //Revisar todos los LocalDate en caso de fallas

    @Mock
    private PacienteRepository pacienteRepository;


    @InjectMocks
    private PacienteService pacienteService;

    @Test
    public void cuandoSeCreaElPrimerPacienteCaso_debeDevolverCA0001() {

        Paciente pacienteNuevo = new Paciente(null, "Test Caso", "test@c.cl", "dir", "Caso", LocalDate.of(2025, 01, 01));

        when(pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CA"))
                .thenReturn(Optional.empty());

        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Paciente> response = pacienteService.createPost(pacienteNuevo);

        assertThat(response.getStatusCode().value()).isEqualTo(201); // 201 Created
        assertThat(response.getBody().getIdPaciente()).isEqualTo("CA0001");
    }

    @Test
    public void cuandoYaExisteCA0001_debeDevolverCA0002() {

        Paciente pacienteNuevo = new Paciente(null, "Test Caso 2", "test2@c.cl", "dir2", "Caso", LocalDate.of(2025, 02, 01));

        Paciente pacienteExistente = new Paciente("CA0001", "Test Caso 1", "test1@c.cl", "dir1", "Caso", LocalDate.of(2025, 03, 02));

        when(pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CA"))
                .thenReturn(Optional.of(pacienteExistente));
  
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Paciente> response = pacienteService.createPost(pacienteNuevo);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
     
        assertThat(response.getBody().getIdPaciente()).isEqualTo("CA0002");
    }
    
    @Test
    public void cuandoSeCreaElPrimerPacienteControl_debeDevolverCR0001() {
        
        Paciente pacienteNuevo = new Paciente(null, "Test Control", "test@r.cl", "dir", "Control", LocalDate.of(2025, 04, 01));
        
        when(pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CR"))
                .thenReturn(Optional.empty());
   
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Paciente> response = pacienteService.createPost(pacienteNuevo);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody().getIdPaciente()).isEqualTo("CR0001");
    }

    @Test
    public void cuandoElTipoDePacienteEsNulo_debeDevolverBadRequest() {

        Paciente pacienteMalo = new Paciente(null, "Test Malo", "test@m.cl", "dir", null, LocalDate.of(2025, 05, 01)); 

        ResponseEntity<Paciente> response = pacienteService.createPost(pacienteMalo);

        assertThat(response.getStatusCode().value()).isEqualTo(400); 
    }
}
