package com.genrisk.sistema.services;
import lombok.Data;

@Data
public class PutCommand<T, ID> {
    private ID id;
    private T newData;
}
