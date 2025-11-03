package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.DatosClinicos;
import com.genrisk.sistema.model.entity.weakEntityKey.DatosClinicosID;
import com.genrisk.sistema.repository.DatosClinicosRepository;
import org.springframework.stereotype.Service;

@Service
public class DatosClinicosService extends CrudServices<DatosClinicos, DatosClinicosID> {
    public DatosClinicosService(DatosClinicosRepository repo) {
        super(repo);
    }    
}
