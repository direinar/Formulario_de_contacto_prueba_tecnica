package com.formulario.contacto.prueba_tecnica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    public void enviarCorreo(String nombre, String email, String mensajeUsuario) {

        if (remitente == null || remitente.isBlank()) {
            throw new IllegalStateException("Falta configurar MAIL_USERNAME en el entorno.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El correo destino es obligatorio.");
        }

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setFrom(remitente);
        mensaje.setTo(email);
        mensaje.setReplyTo(email);
        mensaje.setSubject("Nuevo contacto desde formulario");

        mensaje.setText(
                "Nombre: " + Objects.toString(nombre, "") + "\n" +
                        "Email: " + email + "\n" +
                        "Mensaje: " + Objects.toString(mensajeUsuario, ""));

        mailSender.send(mensaje);
    }
}
