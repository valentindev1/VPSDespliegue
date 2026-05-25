package com.example.demo.repository;

import com.example.demo.model.articulo.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    Optional<Articulo> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Articulo> findByCategoriaId(Long categoriaId);

    List<Articulo> findByPublicada(Boolean publicada);

    List<Articulo> findByCategoriaIdAndPublicada(Long categoriaId, Boolean publicada);

    List<Articulo> findAllByOrderByFechaPublicacionDesc();

    List<Articulo> findTop5ByPublicadaTrueOrderByFechaPublicacionDesc();

    Optional<Articulo> findBySlugAndCategoriaId(String slug, Long categoriaId);
}

