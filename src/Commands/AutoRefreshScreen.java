package src.Commands;

import java.io.PrintWriter;
import java.util.Objects;
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
     * @param windows La ventana principal (JFrame) para realizar la actualización gráfica.
     */
    public AutoRefreshScreen(String command, JFrame windows) {
        super(command, windows);
    }

    /**
     * Ejecuta el comando "autoUpdateScreen", que activa o desactiva la actualización automática de la pantalla.
     *
     * El formato del comando es "autoUpdateScreen&lt;true/false&gt;", donde:
     * - true: activa la actualización automática de la pantalla.
     * - false: desactiva la actualización automática de la pantalla.
     *
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
    public Objects exec(PrintWriter out, Pair<Groups, Users> dataUser) {
        if (
                this.id.equalsIgnoreCase("autoUpdateScreen")) {
            ParamFormatter param = new ParamFormatter(this.params);
            GUI.update_thread.setEstatus(param.asBoolean(0));
            this.ret_client(out); // retornar
        }
        return null;
    }
}
