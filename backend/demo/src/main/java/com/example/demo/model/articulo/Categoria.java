package com.example.demo.model.articulo;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "categorias")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // NOTICIA, POST, EMPRESARIAL

    private String descripcion;

    private String slug;

}
