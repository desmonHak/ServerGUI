package src.Commands;

import java.io.PrintWriter;
import src.ACL.Groups;
import src.ACL.Users;
import src.GUI;
import src.Pair;

public class GetKeyboard extends Command {
    /**
     * Constructor que inicializa un comando a partir de una cadena recibida.
     * Analiza la cadena para extraer el identificador del comando y sus parámetros.
     *
     * @param command La cadena que representa el comando completo.
     * @param gui La ventana de la interfaz gráfica donde se ejecutará el comando.
     */
    public GetKeyboard(String command, GUI gui) {
        super(command, gui);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        if (this.id.equalsIgnoreCase("getKeyboard")
                && does_he_have_permissions(dataUser, this.getClass())) {
            // getKeyboard<>
            this.ret_client(out, "\"%s\"".formatted(gui.BufferKeboard));
        }
        return null;
    }
}
