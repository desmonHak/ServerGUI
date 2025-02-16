package src.Commands;

import src.ACL.Groups;
import src.ACL.Users;
import src.GUI;
import src.Pair;

import javax.swing.*;
import java.io.PrintWriter;

public class ResetKeyboard extends Command {
    /**
     * Constructor que inicializa un comando a partir de una cadena recibida.
     * Analiza la cadena para extraer el identificador del comando y sus parámetros.
     *
     * @param command La cadena que representa el comando completo.
     * @param windows La ventana de la interfaz gráfica donde se ejecutará el comando.
     */
    public ResetKeyboard(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        if (this.id.equalsIgnoreCase("resetKeyboard")
                && does_he_have_permissions(dataUser, this.getClass())) {
            // getKeyboard<>
            GUI.BufferKeboard = ""; // variar el buffer
        }
        this.ret_client(out); // retornar
        return null;
    }
}
