package src.Commands;

import java.io.PrintWriter;
import javax.swing.*;

import src.ACL.Groups;
import src.ACL.Users;
import src.GUI;
import src.Pair;

/**
 * Esta clase extiende {@link Command} y se encarga de procesar el comando para activar o desactivar la actualización automática de la pantalla.
 * El comando "autoUpdateScreen" toma un parámetro booleano que indica si la pantalla debe actualizarse automáticamente.
 *
 * Ejemplo de comando:
 * <pre>
 * autoUpdateScreen&lt;true&gt;
 * </pre>
 * Este comando habilitaría la actualización automática de la pantalla.
 */
public class AutoRefreshScreen extends Command {

    /**
     * Constructor de la clase {@link AutoRefreshScreen}.
     *
     * @param command El comando como una cadena de texto que se procesa para extraer los parámetros.
     * @param gui La ventana principal para realizar la actualización gráfica.
     */
    public AutoRefreshScreen(String command, GUI gui) {
        super(command, gui);
    }

    /**
     * Ejecuta el comando "autoUpdateScreen", que activa o desactiva la actualización automática de la pantalla.
     * <p>
     * El formato del comando es "autoUpdateScreen&lt;true/false&gt;", donde:
     * - true: activa la actualización automática de la pantalla.
     * - false: desactiva la actualización automática de la pantalla.
     * <p>
     * Ejemplo:
     * Si el comando recibido es:
     * <pre>
     * autoUpdateScreen&lt;true&gt;
     * </pre>
     * Este método activará la actualización automática de la pantalla.
     *
     * @return null
     */
    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser) {
        if (
                this.id.equalsIgnoreCase("autoUpdateScreen")
                && does_he_have_permissions(dataUser, this.getClass())) {
            ParamFormatter param = new ParamFormatter(this.params);
            gui.update_thread.setEstatus(param.asBoolean(0));
        }
        this.ret_client(out); // retornar
        return null;
    }
}
