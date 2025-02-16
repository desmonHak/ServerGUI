package src.Commands;

import src.ACL.Groups;
import src.ACL.Users;
import src.Pair;

import javax.swing.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representa un comando recibido desde el cliente para ser procesado y ejecutado.
 * La clase se encarga de analizar el comando, extraer su identificador y parámetros, y proporcionar
 * los medios para ejecutar el comando correspondiente.
 */
public class Command {

    /** El comando completo recibido como una cadena */
    public String command;

    /** El identificador del comando */
    public String           id;

    /** Lista de parámetros extraídos del comando */
    public List<String> params;

    /** Ventana de la interfaz gráfica */
    JFrame     windows;

    /**
     * Constructor que inicializa un comando a partir de una cadena recibida.
     * Analiza la cadena para extraer el identificador del comando y sus parámetros.
     *
     * @param command La cadena que representa el comando completo.
     * @param windows La ventana de la interfaz gráfica donde se ejecutará el comando.
     */
    public Command(String command, JFrame windows) {
        this.command = command;

        String[] commands = this.command.split(":\\s*");
        for (String command_ : commands) {
            // Analizar cada comando
            Pattern commandPattern = Pattern.compile("(\\w+)<(.*)>");
            Matcher matcher = commandPattern.matcher(command_);
            if (matcher.find()) {
                this.id = matcher.group(1);
                params = getCompositeParameters(matcher.group(2));
            }
        }
        this.windows = windows;
    }

    /**
     * Convierte el comando a una representación en cadena.
     *
     * @return Una cadena que representa el comando, con su identificador y parámetros.
     */
    @Override
    public String toString() {
        return "%s<%s>".formatted(this.id, this.params);
    }

    /**
     * Analiza y extrae los parámetros compuestos (por ejemplo, con comas y paréntesis) de la cadena
     * de parámetros que sigue al identificador del comando.
     *
     * @param params La cadena de parámetros a analizar.
     * @return Una lista de cadenas que representan los parámetros extraídos.
     */
    private List<String> getCompositeParameters(String params) {
        List<String> paramList = new ArrayList<>();
        StringBuilder currentParam = new StringBuilder();
        int parenthesesCount = 0;

        for (char c : params.toCharArray()) {
            if (c == ',' && parenthesesCount == 0) {
                paramList.add(currentParam.toString().trim());
                currentParam = new StringBuilder();
            } else {
                currentParam.append(c);
                if (c == '(') parenthesesCount++;
                if (c == ')') parenthesesCount--;
            }
        }
        paramList.add(currentParam.toString().trim());
        return  paramList;
    }

    /**
     * Método que se supone que ejecutará el comando, aunque en esta clase base simplemente retorna null.
     * Las clases que extiendan esta clase deben sobrescribir este método para implementar la ejecución real.
     *
     * @return null, ya que no hay implementación real de la ejecución en esta clase base.
     */
    public Objects exec(){
        return null;
    }
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        return null;
    }

    /**
     *
     * @return true si tiene permisos para ejecutar el comando y false si no
     */
    public boolean does_he_have_permissions(Pair<Groups, Users> dataUser, Class<? extends Command>class_call) {
        Groups group = dataUser.getFirst();
        Users  user  = dataUser.getSecond();
        Boolean data = user.commands_permission.get(class_call);
        // este comando extrañamente no se definio

        // retornar el permiso de este comando:
        return Objects.requireNonNullElse(data, false);
        //user.init_commands_permission();
    }

    /*
     * Permite devolver datos a un cliente desde una clase que herede de comando.
     */
    public void ret_client(PrintWriter out) {
        out.println("return[%s]<>".formatted(this.id));
    }
    public void ret_client(PrintWriter out, String data_client) {
        out.println("return[%s]<%s>".formatted(this.id, data_client));
    }

}
