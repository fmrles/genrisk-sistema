package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.FactDietariosAmbientales;
import com.genrisk.sistema.model.entity.weakEntityKey.FactDietariosAmbientalesID;
import com.genrisk.sistema.repository.FactDietariosAmbientalesRepository;
import org.springframework.stereotype.Service;

@Service
public class FactDietariosAmbientalesService extends CrudServices<FactDietariosAmbientales, FactDietariosAmbientalesID> {

    public FactDietariosAmbientalesService(FactDietariosAmbientalesRepository repo) {
        super(repo);
    }
    
}
