package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.IngresaDatos;
import com.genrisk.sistema.repository.IngresaDatosRepository;
import org.springframework.stereotype.Service;

@Service
public class IngresaDatosService extends CrudServices<IngresaDatos, Integer> {
    public IngresaDatosService(IngresaDatosRepository repo) {
        super(repo);
    }

}
