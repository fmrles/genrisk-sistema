package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.DicotConjunto;
import com.genrisk.sistema.repository.DicotConjuntoRepository;
import org.springframework.stereotype.Service;

@Service
public class DicotConjuntoService extends CrudServices<DicotConjunto, Integer> {
    
    public DicotConjuntoService(DicotConjuntoRepository dicotConjuntoRepository) {
        super(dicotConjuntoRepository);
    }
}
