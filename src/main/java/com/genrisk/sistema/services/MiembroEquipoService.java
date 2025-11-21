package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.MiembroEquipo;
import com.genrisk.sistema.repository.MiembroEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.genrisk.sistema.model.dto.LoginRequest;

import java.util.Optional;

@Service
public class MiembroEquipoService extends CrudServices<MiembroEquipo, Integer> {
    public MiembroEquipoService(MiembroEquipoRepository repo) {
        super(repo);
    }
    @Autowired
    private MiembroEquipoRepository miembroEquipoRepository;

    public Optional<MiembroEquipo> login(LoginRequest loginRequest) {
        Optional<MiembroEquipo> miembroOpt = miembroEquipoRepository.findByCorreoMiembro(loginRequest.getCorreo());

        if (miembroOpt.isEmpty()) {
            return Optional.empty();
        }

        MiembroEquipo miembro = miembroOpt.get();

        if (loginRequest.getClave().equals(miembro.getClave())) {
            return Optional.of(miembro);
        } else {
            return Optional.empty();
        }
    }
}
