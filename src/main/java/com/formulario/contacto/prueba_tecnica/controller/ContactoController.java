package com.formulario.contacto.prueba_tecnica.controller;

import com.formulario.contacto.prueba_tecnica.dto.ContactoDTO;
import com.formulario.contacto.prueba_tecnica.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*")
public class ContactoController {

    private final EmailService emailService;

    public ContactoController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> enviar(@RequestBody ContactoDTO dto) {
        String nombre = dto.getNombre() == null ? "" : dto.getNombre().trim();
        String email = dto.getEmail() == null ? "" : dto.getEmail().trim();
        String mensaje = dto.getMensaje() == null ? "" : dto.getMensaje().trim();

        if (nombre.length() < 2 || nombre.length() > 80) {
            return ResponseEntity.badRequest().body("El nombre debe tener entre 2 y 80 caracteres");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body("El correo destino no tiene un formato valido");
        }
        if (mensaje.length() < 5 || mensaje.length() > 1000) {
            return ResponseEntity.badRequest().body("El mensaje debe tener entre 5 y 1000 caracteres");
        }

        try {
            emailService.enviarCorreo(
                    nombre,
                    email,
                    mensaje);
            return ResponseEntity.ok("Correo enviado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se pudo enviar el correo. Verifica MAIL_USERNAME y MAIL_PASSWORD.");
        }
    }
}
