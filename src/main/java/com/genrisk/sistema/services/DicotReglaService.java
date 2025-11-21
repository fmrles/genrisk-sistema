package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.DicotRegla;
import com.genrisk.sistema.repository.DicotReglaRepository;
import org.springframework.stereotype.Service;

@Service
public class DicotReglaService extends CrudServices<DicotRegla, Integer> {
    
    public DicotReglaService(DicotReglaRepository dicotReglaRepository) {
        super(dicotReglaRepository);
    }
    
}
