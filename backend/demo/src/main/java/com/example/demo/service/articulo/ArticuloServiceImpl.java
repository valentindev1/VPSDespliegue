package com.example.demo.service.articulo;

import com.example.demo.dto.articulo.ArticuloRequestDTO;
import com.example.demo.dto.articulo.ArticuloResponseDTO;
import com.example.demo.model.articulo.*;
import com.example.demo.model.usuario.Usuario;
import com.example.demo.repository.ArticuloRepository;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticuloServiceImpl implements ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    private final String UPLOAD_DIR = "uploads/articulos/";

    // =========================
    // ✅ CREAR
    // =========================
    @Override
    public ArticuloResponseDTO crear(ArticuloRequestDTO dto) {

        Articulo articulo = new Articulo();

        articulo.setTitulo(dto.getTitulo());
        articulo.setContenido(dto.getContenido());
        articulo.setResumen(dto.getResumen());

        // ✅ Categoría
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        articulo.setCategoria(categoria);

        // ✅ Usuario (temporal)
        Usuario usuario = usuarioRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        articulo.setUsuario(usuario);

        // ✅ Publicada
        articulo.setPublicada(dto.getPublicada());

        // ✅ Slug
        articulo.setSlug(generarSlugUnico(dto.getTitulo()));

        // ✅ Imágenes
        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {

            List<ArticuloImagen> imagenes = dto.getImagenes().stream().map(file -> {

                String ruta = guardarImagen(file);

                ArticuloImagen img = new ArticuloImagen();
                img.setUrl(ruta);
                img.setArticulo(articulo);

                return img;

            }).collect(Collectors.toList());

            articulo.setImagenes(imagenes);
        }

        Articulo guardado = articuloRepository.save(articulo);

        return mapToDTO(guardado);
    }

    // =========================
    // ✅ LISTAR
    // =========================
    @Override
    public List<ArticuloResponseDTO> listar() {

        return articuloRepository.findAllByOrderByFechaPublicacionDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // ✅ OBTENER POR ID
    // =========================
    @Override
    public ArticuloResponseDTO obtenerPorId(Long id) {

        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        return mapToDTO(articulo);
    }

    // =========================
    // ✅ OBTENER POR SLUG
    // =========================
    @Override
    public ArticuloResponseDTO obtenerPorSlug(String slug) {

        Articulo articulo = articuloRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        return mapToDTO(articulo);
    }

    // =========================
    // ✅ CATEGORIA + SLUG
    // =========================
    @Override
    public ArticuloResponseDTO obtenerPorCategoriaYSlug(String categoriaSlug, String articuloSlug) {

        Categoria categoria = categoriaRepository.findBySlug(categoriaSlug)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Articulo articulo = articuloRepository
                .findBySlugAndCategoriaId(articuloSlug, categoria.getId())
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        return mapToDTO(articulo);
    }

    // =========================
    // ✅ ACTUALIZAR
    // =========================
    @Override
    public ArticuloResponseDTO actualizar(Long id, ArticuloRequestDTO dto) {

        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        boolean tituloCambio = !articulo.getTitulo().equals(dto.getTitulo());

        articulo.setTitulo(dto.getTitulo());
        articulo.setContenido(dto.getContenido());
        articulo.setResumen(dto.getResumen());

        // ✅ Categoría
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        articulo.setCategoria(categoria);

        articulo.setPublicada(dto.getPublicada());

        // ✅ Slug
        if (tituloCambio) {
            articulo.setSlug(generarSlugUnico(dto.getTitulo()));
        }

        // ✅ Reemplazar imágenes
        articulo.getImagenes().clear();

        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {

            dto.getImagenes().forEach(file -> {

                String ruta = guardarImagen(file);

                ArticuloImagen img = new ArticuloImagen();
                img.setUrl(ruta);
                img.setArticulo(articulo);

                articulo.getImagenes().add(img);
            });
        }

        Articulo actualizado = articuloRepository.save(articulo);

        return mapToDTO(actualizado);
    }

    // =========================
    // ✅ ELIMINAR
    // =========================
    @Override
    public void eliminar(Long id) {
        articuloRepository.deleteById(id);
    }

    // =========================
    // 🔥 GUARDAR IMAGEN (solo tamaño)
    // =========================
    private String guardarImagen(MultipartFile file) {

        try {
            // ✅ SOLO VALIDAR TAMAÑO
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new RuntimeException("La imagen no puede superar 10MB");
            }

            String nombre = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path ruta = Paths.get(UPLOAD_DIR).resolve(nombre);

            Files.createDirectories(ruta.getParent());

            Files.copy(file.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/articulos/" + nombre;

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la imagen");
        }
    }

    // =========================
    // 🔥 MAPEO
    // =========================
    private ArticuloResponseDTO mapToDTO(Articulo articulo) {

        ArticuloResponseDTO dto = new ArticuloResponseDTO();

        dto.setId(articulo.getId());
        dto.setTitulo(articulo.getTitulo());
        dto.setContenido(articulo.getContenido());
        dto.setResumen(articulo.getResumen());
        dto.setSlug(articulo.getSlug());
        dto.setPublicada(articulo.getPublicada());
        dto.setFechaPublicacion(articulo.getFechaPublicacion());

        dto.setCategoria(articulo.getCategoria().getNombre());
        dto.setAutor(articulo.getUsuario().getNombre());

        List<String> imagenes = articulo.getImagenes()
                .stream()
                .map(ArticuloImagen::getUrl)
                .collect(Collectors.toList());

        dto.setImagenes(imagenes);

        return dto;
    }

    // =========================
    // 🔥 SLUG
    // =========================
    private String generarSlug(String titulo) {
        return titulo.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }

    private String generarSlugUnico(String titulo) {

        String base = generarSlug(titulo);
        String slug = base;
        int i = 1;

        while (articuloRepository.existsBySlug(slug)) {
            slug = base + "-" + i;
            i++;
        }

        return slug;
    }
}