package com.vaxi.spring_boot_microservice_1.inmueble.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Clase del tipo entidad (Entity)
 * Tiene relación con una tabla en postgres
 * Java persistence API Entity
 */
@Data //Genera automaticamente el Getter & Setter
@Entity
@Table(name="inmueble")
public class Inmueble {
    @Id //Desde la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Autoincrementable
    private Long id;
    @Column(name="nombre", length=150, nullable = false)
    private String nombre;
    @Column(name="direccion", length=500, nullable = false)
    private String direccion;
    @Column(name="foto", length=1200,nullable = true)
    private String picture;

    @Column(name="precio", nullable = false)
    private Double precio;
    @Column(name="fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}

