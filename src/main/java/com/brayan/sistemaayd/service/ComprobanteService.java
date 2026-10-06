// com.lixy.sistemaayd/service/ComprobanteService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.DetalleVenta;
import com.brayan.sistemaayd.entity.Venta;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class ComprobanteService {

    public byte[] generarComprobantePdf(Venta venta) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // ============================================
        // 1. FUENTES
        // ============================================
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.DARK_GRAY);
        Font subtituloFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY);
        Font negritaFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        Font totalFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(102, 126, 234));

        // ============================================
        // 2. ENCABEZADO
        // ============================================
        Paragraph titulo = new Paragraph("REPUESTOS MOTOS", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph("Sistema de Gestión de Ventas", subtituloFont);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitulo);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // ============================================
        // 3. DATOS DE LA EMPRESA
        // ============================================
        PdfPTable infoEmpresa = new PdfPTable(2);
        infoEmpresa.setWidthPercentage(100);
        infoEmpresa.setSpacingAfter(10);

        PdfPCell cellEmpresa = new PdfPCell();
        cellEmpresa.setBorder(Rectangle.NO_BORDER);
        cellEmpresa.addElement(new Paragraph("RUC: 20512345678", normalFont));
        cellEmpresa.addElement(new Paragraph("Dirección: Av. Principal 123", normalFont));
        cellEmpresa.addElement(new Paragraph("Teléfono: 987-654-321", normalFont));
        cellEmpresa.addElement(new Paragraph("Email: ventas@repuestos.com", normalFont));
        infoEmpresa.addCell(cellEmpresa);

        PdfPCell cellDatos = new PdfPCell();
        cellDatos.setBorder(Rectangle.NO_BORDER);
        cellDatos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        cellDatos.addElement(new Paragraph("Fecha: " + venta.getFechaVenta().format(formatter), normalFont));
        cellDatos.addElement(new Paragraph("Comprobante: " + venta.getTipoComprobante(), normalFont));
        cellDatos.addElement(new Paragraph("N°: " + venta.getNumeroComprobante(), normalFont));
        cellDatos.addElement(new Paragraph("Código: " + venta.getCodigoVenta(), normalFont));
        infoEmpresa.addCell(cellDatos);

        document.add(infoEmpresa);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("-----------------------------------------------------------------", normalFont));

        // ============================================
        // 4. DATOS DEL CLIENTE
        // ============================================
        PdfPTable infoCliente = new PdfPTable(1);
        infoCliente.setWidthPercentage(100);
        infoCliente.setSpacingAfter(10);

        PdfPCell cellCliente = new PdfPCell();
        cellCliente.setBorder(Rectangle.BOX);
        cellCliente.setPadding(8);
        cellCliente.setBackgroundColor(new BaseColor(245, 245, 245));

        String nombreCliente = venta.getCliente() != null ? venta.getCliente().getNombreCompleto() : "Cliente General";
        cellCliente.addElement(new Paragraph("CLIENTE", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.DARK_GRAY)));
        cellCliente.addElement(new Paragraph("   Nombre: " + nombreCliente, normalFont));

        if (venta.getCliente() != null) {
            cellCliente.addElement(new Paragraph("   Documento: " + venta.getCliente().getTipoDocumento() + " - " +
                                   venta.getCliente().getNumeroDocumento(), normalFont));
            cellCliente.addElement(new Paragraph("   Teléfono: " + (venta.getCliente().getTelefono() != null ? venta.getCliente().getTelefono() : "No registrado"), normalFont));
            cellCliente.addElement(new Paragraph("   Email: " + (venta.getCliente().getEmail() != null ? venta.getCliente().getEmail() : "No registrado"), normalFont));
        }

        infoCliente.addCell(cellCliente);
        document.add(infoCliente);

        document.add(new Paragraph(" "));

        // ============================================
        // 5. PRODUCTOS
        // ============================================
        PdfPTable tablaProductos = new PdfPTable(5);
        tablaProductos.setWidthPercentage(100);
        tablaProductos.setSpacingAfter(10);
        tablaProductos.setWidths(new float[]{35, 10, 15, 20, 20});

        String[] headers = {"Producto", "Cant.", "Precio", "Subtotal", "Total"};
        for (String h : headers) {
            PdfPCell headerCell = new PdfPCell(new Paragraph(h, negritaFont));
            headerCell.setBackgroundColor(new BaseColor(102, 126, 234));
            headerCell.setPadding(8);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBorder(Rectangle.BOX);
            tablaProductos.addCell(headerCell);
        }

        if (venta.getDetalles() != null) {
            for (DetalleVenta d : venta.getDetalles()) {
                tablaProductos.addCell(new PdfPCell(new Paragraph(d.getProducto().getNombreProducto(), normalFont)));
                tablaProductos.addCell(new PdfPCell(new Paragraph(d.getCantidad().toString(), normalFont)));
                tablaProductos.addCell(new PdfPCell(new Paragraph("$ " + d.getPrecioUnitario().toString(), normalFont)));
                tablaProductos.addCell(new PdfPCell(new Paragraph("$ " + d.getSubtotal().toString(), normalFont)));
                tablaProductos.addCell(new PdfPCell(new Paragraph("$ " + d.getTotal().toString(), normalFont)));
            }
        }

        document.add(tablaProductos);

        // ============================================
        // 6. TOTALES
        // ============================================
        PdfPTable tablaTotales = new PdfPTable(2);
        tablaTotales.setWidthPercentage(40);
        tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.setSpacingAfter(10);

        PdfPCell cellSubtotalLabel = new PdfPCell(new Paragraph("Subtotal:", normalFont));
        cellSubtotalLabel.setBorder(Rectangle.NO_BORDER);
        cellSubtotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(cellSubtotalLabel);

        PdfPCell cellSubtotal = new PdfPCell(new Paragraph("$ " + venta.getSubtotal().toString(), normalFont));
        cellSubtotal.setBorder(Rectangle.NO_BORDER);
        cellSubtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(cellSubtotal);

        PdfPCell cellDescuentoLabel = new PdfPCell(new Paragraph("Descuento:", normalFont));
        cellDescuentoLabel.setBorder(Rectangle.NO_BORDER);
        cellDescuentoLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(cellDescuentoLabel);

        PdfPCell cellDescuento = new PdfPCell(new Paragraph("$ " + (venta.getDescuento() != null ? venta.getDescuento().toString() : "0.00"), normalFont));
        cellDescuento.setBorder(Rectangle.NO_BORDER);
        cellDescuento.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(cellDescuento);

        PdfPCell cellIgvLabel = new PdfPCell(new Paragraph("IVA (18%):", normalFont));
        cellIgvLabel.setBorder(Rectangle.NO_BORDER);
        cellIgvLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(cellIgvLabel);

        PdfPCell cellIgv = new PdfPCell(new Paragraph("$ " + venta.getIgv().toString(), normalFont));
        cellIgv.setBorder(Rectangle.NO_BORDER);
        cellIgv.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(cellIgv);

        PdfPCell cellTotalLabel = new PdfPCell(new Paragraph("TOTAL:", totalFont));
        cellTotalLabel.setBorder(Rectangle.NO_BORDER);
        cellTotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(cellTotalLabel);

        PdfPCell cellTotal = new PdfPCell(new Paragraph("$ " + venta.getTotal().toString(), totalFont));
        cellTotal.setBorder(Rectangle.NO_BORDER);
        cellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(cellTotal);

        document.add(tablaTotales);

        // ============================================
        // 7. PIE DE PÁGINA
        // ============================================
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph pie = new Paragraph("Gracias por su compra. ¡Vuelva pronto!", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY));
        pie.setAlignment(Element.ALIGN_CENTER);
        document.add(pie);

        Paragraph pie2 = new Paragraph("Este documento es un comprobante de venta electrónico válido.", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GRAY));
        pie2.setAlignment(Element.ALIGN_CENTER);
        document.add(pie2);

        document.close();
        return outputStream.toByteArray();
    }
}