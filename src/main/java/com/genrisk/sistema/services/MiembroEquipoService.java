package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.MiembroEquipo;
import com.genrisk.sistema.repository.MiembroEquipoRepository;
import org.springframework.stereotype.Service;

@Service
public class MiembroEquipoService extends CrudServices<MiembroEquipo, Integer> {
    public MiembroEquipoService(MiembroEquipoRepository repo) {
        super(repo);
    }
}
