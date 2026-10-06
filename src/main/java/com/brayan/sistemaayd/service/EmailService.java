// com.lixy.sistemaayd/service/EmailService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.Venta;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ComprobanteService comprobanteService;

    /**
     * Envía el comprobante de venta al correo del cliente
     */
    public void enviarComprobante(Venta venta) throws Exception {
        // Obtener email del cliente
        String emailCliente = null;
        if (venta.getCliente() != null) {
            emailCliente = venta.getCliente().getEmail();
        }

        // Si el cliente no tiene email, lanzar excepción
        if (emailCliente == null || emailCliente.isEmpty()) {
            throw new Exception("El cliente no tiene un correo electrónico registrado");
        }

        enviarComprobante(venta, emailCliente);
    }

    /**
     * Envía el comprobante de venta a un correo específico
     */
    public void enviarComprobante(Venta venta, String email) throws Exception {
        // Validar que el email no esté vacío
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("El correo electrónico no puede estar vacío");
        }

        // Validar que el email tenga formato válido
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new Exception("El correo electrónico no tiene un formato válido");
        }

        try {
            // 1. Generar PDF
            byte[] pdf = comprobanteService.generarComprobantePdf(venta);

            // 2. Crear el mensaje
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            // 3. Configurar destinatarios
            helper.setTo(email.trim());
            helper.setSubject("Comprobante de Venta - " + venta.getCodigoVenta());

            // 4. Crear cuerpo del correo (HTML)
            Context context = new Context();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Datos del cliente
            String nombreCliente = venta.getCliente() != null && venta.getCliente().getNombreCompleto() != null
                    ? venta.getCliente().getNombreCompleto() : "Cliente General";
            String telefonoCliente = venta.getCliente() != null && venta.getCliente().getTelefono() != null
                    ? venta.getCliente().getTelefono() : "No registrado";

            context.setVariable("cliente", nombreCliente);
            context.setVariable("emailCliente", email.trim());
            context.setVariable("telefonoCliente", telefonoCliente);
            context.setVariable("codigoVenta", venta.getCodigoVenta());
            context.setVariable("fecha", venta.getFechaVenta().format(formatter));
            context.setVariable("metodoPago", venta.getMetodoPago() != null ? venta.getMetodoPago() : "No especificado");
            context.setVariable("tipoComprobante", venta.getTipoComprobante() != null ? venta.getTipoComprobante() : "Boleta");
            context.setVariable("numeroComprobante", venta.getNumeroComprobante() != null ? venta.getNumeroComprobante() : "N/A");
            context.setVariable("subtotal", venta.getSubtotal() != null ? venta.getSubtotal().toString() : "0.00");
            context.setVariable("igv", venta.getIgv() != null ? venta.getIgv().toString() : "0.00");
            context.setVariable("total", venta.getTotal() != null ? venta.getTotal().toString() : "0.00");
            context.setVariable("detalles", venta.getDetalles());

            String htmlContent = templateEngine.process("email_comprobante", context);
            helper.setText(htmlContent, true);

            // 5. Adjuntar PDF
            helper.addAttachment("comprobante_" + venta.getCodigoVenta() + ".pdf",
                    new ByteArrayDataSource(pdf, "application/pdf"));

            // 6. Enviar
            mailSender.send(mensaje);

        } catch (jakarta.mail.MessagingException e) {
            throw new Exception("Error al enviar el correo: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error al generar o enviar el comprobante: " + e.getMessage());
        }
    }
}