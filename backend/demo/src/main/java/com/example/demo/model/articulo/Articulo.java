package com.example.demo.model.articulo;

import com.example.demo.model.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data

@Table(name = "articulo", indexes = {
        @Index(name = "idx_slug", columnList = "slug"),
        @Index(name = "idx_categoria", columnList = "categoria_id")
})

public class Articulo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;


    @Column(length = 500)
    private String resumen;


    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;


    @Column(unique = true, nullable = false)
    private String slug;



    @Column(nullable = false)
    private Boolean publicada;



    @OneToMany(mappedBy = "articulo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ArticuloImagen> imagenes = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDate fechaPublicacion;


    @PrePersist
    public void prePersist() {
        this.fechaPublicacion = LocalDate.now();
    }

}
