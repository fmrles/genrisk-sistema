package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.MiembroEquipo;
import com.genrisk.sistema.repository.MiembroEquipoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.genrisk.sistema.model.dto.LoginRequest;

import java.util.Optional;

@Service
public class MiembroEquipoService extends CrudServices<MiembroEquipo, Integer> {
    public MiembroEquipoService(MiembroEquipoRepository repo, PasswordEncoder passwordEncoder) {
        super(repo);
        this.miembroEquipoRepository = repo;
        this.passwordEncoder = passwordEncoder;
    }
    
    private MiembroEquipoRepository miembroEquipoRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<MiembroEquipo> login(LoginRequest loginRequest) {
        Optional<MiembroEquipo> miembroOpt = miembroEquipoRepository.findByCorreoMiembro(loginRequest.getCorreo());

        if (miembroOpt.isEmpty()) {
            return Optional.empty();
        }

        MiembroEquipo miembro = miembroOpt.get();

        if (passwordEncoder.matches(loginRequest.getClave(), miembro.getClave())) {
            return Optional.of(miembro);
        } else {
            return Optional.empty();
        }
    }

    public MiembroEquipo crearNuevoMiembro(MiembroEquipo nuevoMiembroEquipo){
        if(nuevoMiembroEquipo.getClave() != null){
            if(!nuevoMiembroEquipo.getClave().startsWith("$2a$") && !nuevoMiembroEquipo.getClave().startsWith("$2y$")){
                nuevoMiembroEquipo.setClave(nuevoMiembroEquipo.getClave());
            }
        }
        return miembroEquipoRepository.save(nuevoMiembroEquipo);
    }

    public void cambiarClave(MiembroEquipo miembro, String nuevaClave) {
        miembro.setClave(nuevaClave); // hasheo de forma automática
        miembroEquipoRepository.save(miembro);
    }
}
