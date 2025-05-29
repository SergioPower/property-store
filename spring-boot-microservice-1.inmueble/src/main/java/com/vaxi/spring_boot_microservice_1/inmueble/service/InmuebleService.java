package com.vaxi.spring_boot_microservice_1.inmueble.service;

import com.vaxi.spring_boot_microservice_1.inmueble.model.Inmueble;

import java.util.List;

public interface InmuebleService {
    //
    Inmueble saveInmueble(Inmueble inmueble);

    void deleteInmueble(Long inmuebleId);

    List<Inmueble> findAllInmuebles();
}
