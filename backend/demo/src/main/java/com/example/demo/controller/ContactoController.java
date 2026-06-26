package com.example.demo.controller;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/contacto")

public class ContactoController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping(value = "/trabaja-con-nosotros", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> enviarHojaDeVida(
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("correo") String correo,
            @RequestParam("telefono") String telefono,
            @RequestParam("mensaje") String mensaje,
            @RequestParam("cv") MultipartFile cv) {

        Map<String, String> respuesta = new HashMap<>();

        try {
            // --- 1. VALIDACIONES DE SEGURIDAD DEL ARCHIVO ---

            // A. Validar que el archivo exista y no esté vacío
            if (cv == null || cv.isEmpty()) {
                respuesta.put("status", "error");
                respuesta.put("message", "Debes adjuntar un archivo (Hoja de vida).");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }

            // B. Validar el tipo de archivo (Solo Word o PDF)
            String contentType = cv.getContentType();
            boolean isValidType = contentType != null && (
                    contentType.equals("application/pdf") ||
                            contentType.equals("application/msword") ||
                            contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            );

            if (!isValidType) {
                respuesta.put("status", "error");
                respuesta.put("message", "Formato de archivo no válido. Solo se permiten PDF o Word.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }


            // --- 2. CONSTRUCCIÓN DEL CORREO ELECTRÓNICO ---

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Configurar los destinatarios y el origen
            helper.setTo("talentohumano@hormisursas.com.co"); // A dónde llega

            // ¡ESTA ES LA LÍNEA CLAVE! Debe ser idéntico al username de tu application.properties
            helper.setFrom("talentohumano@hormisursas.com.co");

            helper.setSubject("Nueva Postulación Laboral: " + nombres + " " + apellidos);

            // Diseñar el cuerpo del correo electrónico
            String cuerpoCorreo = String.format(
                    "Has recibido una nueva postulación a través de la página web:\n\n" +
                            "Candidato: %s %s\n" +
                            "Correo de contacto: %s\n" +
                            "Teléfono: %s\n" +
                            "Mensaje/Perfil:\n%s\n\n" +
                            "El currículum se encuentra adjunto a este correo.",
                    nombres, apellidos, correo, telefono, mensaje
            );
            helper.setText(cuerpoCorreo);


            // --- 3. ADJUNTAR ARCHIVO Y ENVIAR ---

            // Adjuntar el archivo directamente desde la RAM
            helper.addAttachment(Objects.requireNonNull(cv.getOriginalFilename()), cv);

            // Enviar el email
            mailSender.send(message);

            // Preparar respuesta exitosa
            respuesta.put("status", "success");
            respuesta.put("message", "Postulación enviada correctamente por correo.");
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace(); // Para ver el error exacto en tu consola de Spring Boot
            respuesta.put("status", "error");
            respuesta.put("message", "No se pudo procesar el envío del correo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }



    @PostMapping("/cliente")
    public ResponseEntity<Map<String, String>> contactoCliente(
            @RequestBody Map<String, String> datos) {

        Map<String, String> respuesta = new HashMap<>();

        try {

            String nombre = datos.get("nombre");
            String telefono = datos.get("telefono");
            String correo = datos.get("correo");
            String descripcion = datos.get("descripcion");

            // Validación básica
            if (nombre == null || telefono == null || descripcion == null) {
                respuesta.put("status", "error");
                respuesta.put("message", "Los campos son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }

            // Crear correo
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            // ✅ AQUÍ EL CAMBIO IMPORTANTE
            helper.setTo("comercial@hormisursas.com.co");

            helper.setFrom("talentohumano@hormisursas.com.co");

            helper.setSubject("Nueva solicitud de cliente - Página Web");

            String cuerpo = String.format(
                    "Nuevo cliente interesado:\n\n" +
                            "Nombre: %s\n" +
                            "Teléfono: %s\n" +
                            "Correo: %s\n" +
                            "Descripción de la obra:\n%s",
                    nombre, telefono, correo, descripcion
            );

            helper.setText(cuerpo);

            mailSender.send(message);

            respuesta.put("status", "success");
            respuesta.put("message", "Solicitud enviada correctamente");

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace(); // luego lo cambias por logger
            respuesta.put("status", "error");
            respuesta.put("message", "Error interno, intenta nuevamente");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }





}