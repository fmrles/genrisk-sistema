package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.DicotValor;
import com.genrisk.sistema.repository.DicotValorRepository;
import org.springframework.stereotype.Service;

@Service
public class DicotValorService extends CrudServices<DicotValor, Integer> {
    
    public DicotValorService(DicotValorRepository dicotValorRepository) {
        super(dicotValorRepository);
    }
    
}
