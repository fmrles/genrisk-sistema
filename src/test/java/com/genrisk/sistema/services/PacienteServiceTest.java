package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) 
public class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;


    @InjectMocks
    private PacienteService pacienteService;

    @Test
    public void cuandoSeCreaElPrimerPacienteCaso_debeDevolverCA0001() {

        Paciente pacienteNuevo = new Paciente(null, "Test Caso", "test@c.cl", "dir", "Caso");

        when(pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CA"))
                .thenReturn(Optional.empty());

        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Paciente> response = pacienteService.createPost(pacienteNuevo);

        assertThat(response.getStatusCode().value()).isEqualTo(201); // 201 Created
        assertThat(response.getBody().getIdPaciente()).isEqualTo("CA0001");
    }

    @Test
    public void cuandoYaExisteCA0001_debeDevolverCA0002() {

        Paciente pacienteNuevo = new Paciente(null, "Test Caso 2", "test2@c.cl", "dir2", "Caso");

        Paciente pacienteExistente = new Paciente("CA0001", "Test Caso 1", "test1@c.cl", "dir1", "Caso");

        when(pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CA"))
                .thenReturn(Optional.of(pacienteExistente));
  
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Paciente> response = pacienteService.createPost(pacienteNuevo);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
     
        assertThat(response.getBody().getIdPaciente()).isEqualTo("CA0002");
    }
    
    @Test
    public void cuandoSeCreaElPrimerPacienteControl_debeDevolverCR0001() {
        
        Paciente pacienteNuevo = new Paciente(null, "Test Control", "test@r.cl", "dir", "Control");
        
        when(pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CR"))
                .thenReturn(Optional.empty());
   
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Paciente> response = pacienteService.createPost(pacienteNuevo);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody().getIdPaciente()).isEqualTo("CR0001");
    }

    @Test
    public void cuandoElTipoDePacienteEsNulo_debeDevolverBadRequest() {

        Paciente pacienteMalo = new Paciente(null, "Test Malo", "test@m.cl", "dir", null); 

        ResponseEntity<Paciente> response = pacienteService.createPost(pacienteMalo);

        assertThat(response.getStatusCode().value()).isEqualTo(400); 
    }
}
