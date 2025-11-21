package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Genotipificacion;
import com.genrisk.sistema.repository.GenotipificacionRepository;
import org.springframework.stereotype.Service;

@Service
public class GenotipificacionService extends CrudServices<Genotipificacion, Integer> {
    public GenotipificacionService(GenotipificacionRepository repo) {
        super(repo);
    }
}
