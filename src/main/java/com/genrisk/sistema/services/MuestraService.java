package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Muestra;
import com.genrisk.sistema.model.entity.weakEntityKey.muestraID;
import com.genrisk.sistema.repository.MuestraRepository;
import org.springframework.stereotype.Service;

@Service
public class MuestraService extends CrudServices<Muestra, muestraID> {
    public MuestraService(MuestraRepository repo) {
        super(repo);
    }
}
