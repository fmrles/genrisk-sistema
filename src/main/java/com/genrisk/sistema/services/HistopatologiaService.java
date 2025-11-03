package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Histopatologia;
import com.genrisk.sistema.model.entity.weakEntityKey.HistopatologiaID;
import com.genrisk.sistema.repository.HistopatologiaRepository;
import org.springframework.stereotype.Service;

@Service
public class HistopatologiaService extends CrudServices<Histopatologia, HistopatologiaID> {
    public HistopatologiaService(HistopatologiaRepository repo) {
        super(repo);
    }
}
