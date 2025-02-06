package src.Commands;

import src.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;

public class GetKeyboard extends Command {
    /**
     * Constructor que inicializa un comando a partir de una cadena recibida.
     * Analiza la cadena para extraer el identificador del comando y sus parámetros.
     *
     * @param command La cadena que representa el comando completo.
     * @param windows La ventana de la interfaz gráfica donde se ejecutará el comando.
     */
    public GetKeyboard(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public Objects exec(PrintWriter out){
        if (this.id.equalsIgnoreCase("getKeyboard")) {
            // getKeyboard<>
            this.ret_client(out, "\"%s\"".formatted(GUI.BufferKeboard));
        }
        return null;
    }
}
