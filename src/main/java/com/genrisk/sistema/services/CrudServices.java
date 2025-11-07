package com.genrisk.sistema.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.List;

public abstract class CrudServices<T, ID> {

    protected final JpaRepository<T, ID> repository;

    protected CrudServices(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    
    public ResponseEntity<T> getById(ID id) {
        Optional<T> getEntidad = repository.findById(id);
        if(getEntidad.isPresent()){
            return ResponseEntity.ok(getEntidad.get());
        }
        return ResponseEntity.notFound().build();
    }

    
    public ResponseEntity<List<T>> getAll() {
        List<T> listadoEntidad = repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listadoEntidad);
    }

    
    public ResponseEntity<T> createPost(T newEntidad) {
        T entidadMostrar = repository.save(newEntidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidadMostrar);
    }

    
    @Transactional
    public ResponseEntity<T> updatePut(PutCommand<T, ID> command) {
        T existente = repository.findById(command.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        T nuevo = command.getNewData();
        if (nuevo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos vacíos");
        }

        copiarCamposNoNulos(nuevo, existente);

        return ResponseEntity.ok(repository.save(existente));
    }

    // ==== Helper: reflection para copiar campos no nulos (evita strings vacías) ====
    private void copiarCamposNoNulos(T fuente, T destino) {
        for (Field campo : fuente.getClass().getDeclaredFields()) {
            campo.setAccessible(true);
            try {
                Object valor = campo.get(fuente);
                if (valor != null) {
                    if (valor instanceof String s && s.trim().isEmpty()) continue;
                    campo.set(destino, valor);
                }
            } catch (Exception ignored) {}
        }
    }

    public ResponseEntity<Void> deleteById(ID id) {
        Optional<T> entidadEliminar = repository.findById(id);
        if (entidadEliminar.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return null;
    }
}


