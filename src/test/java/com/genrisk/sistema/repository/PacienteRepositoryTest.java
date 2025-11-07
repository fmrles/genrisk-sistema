package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.Paciente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PacienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PacienteRepository pacienteRepository;


    @Test
    public void cuandoBuscaUltimoPaciente_yNoHay_devuelveOptionalVacio() {

        Optional<Paciente> encontrado = pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CA");

        assertThat(encontrado).isNotPresent();
    }

    @Test
    public void cuandoBuscaUltimoPaciente_yHayVarios_devuelveElCorrecto() {

        Paciente p1 = new Paciente("CA0001", "Test 1", "t1@c.cl", "dir", "Caso");
        Paciente p2 = new Paciente("CA0002", "Test 2", "t2@c.cl", "dir", "Caso");
        Paciente p3_otro = new Paciente("CR0001", "Control 1", "cr1@c.cl", "dir", "Control");

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(p3_otro);
        entityManager.flush();

        Optional<Paciente> encontrado = pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CA");

        assertThat(encontrado).isPresent();

        assertThat(encontrado.get().getIdPaciente()).isEqualTo("CA0002");
    }
    
    @Test
    public void cuandoBuscaUltimoPaciente_conNumerosDesordenados_devuelveElMayor() {
        Paciente p1 = new Paciente("CA0005", "Test 5", "t5@c.cl", "dir", "Caso");
        Paciente p2 = new Paciente("CA0001", "Test 1", "t1@c.cl", "dir", "Caso");
        
        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.flush();

        Optional<Paciente> encontrado = pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CA");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getIdPaciente()).isEqualTo("CA0005");
    }
}