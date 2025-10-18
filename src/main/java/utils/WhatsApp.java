package utils;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WhatsApp {

    public static void enviarMensaje(String telefono, String mensaje) {
        try {
            String telefonoLimpio = telefono.replaceAll("[^0-9]", "");
            String mensajeCodificado = URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
            String url = "https://wa.me/" + telefonoLimpio + "?text=" + mensajeCodificado;

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}