package com.example.demo.model.articulo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "articulo_imagenes")
@Data
public class ArticuloImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url; // ruta o URL de la imagen

    private String descripcion; // opcional

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    @JsonBackReference
    private Articulo articulo;

}
