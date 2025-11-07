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

    //Que la consulta no falle si la tabla de pacientes está vacía. Simplemente debe devolver "nada" (vacío).
    @Test
    public void cuandoBuscaUltimoPaciente_yNoHay_devuelveOptionalVacio() {

        Optional<Paciente> encontrado = pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc("CA");

        assertThat(encontrado).isNotPresent();
    }

    //Que la consulta filtre correctamente. Al buscar por "CA", debe ignorar a "CR0001" y, de entre "CA0001" y "CA0002", debe devolver "CA0002" (el último).
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
    
    //Que la parte OrderByIdPacienteDesc de la consulta funcione. Inserta "CA0005" y "CA0001" y comprueba que la consulta identifica correctamente a "CA0005" como el "mayor" o "último".
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