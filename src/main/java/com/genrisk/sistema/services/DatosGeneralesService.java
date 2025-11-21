package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.DatosGenerales;
import com.genrisk.sistema.model.entity.weakEntityKey.DatosGeneralesID;
import com.genrisk.sistema.repository.DatosGeneralesRepository;
import org.springframework.stereotype.Service;

@Service
public class DatosGeneralesService extends CrudServices<DatosGenerales, DatosGeneralesID> {

    public DatosGeneralesService(DatosGeneralesRepository repo) {
        super(repo);
    }
    
}
