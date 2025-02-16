package src.Commands;

import src.ACL.Groups;
import src.ACL.Users;
import src.GUI;
import src.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;

/**
 * Esta clase extiende {@link Command} y está encargada de procesar el comando para dibujar un píxel en la interfaz gráfica.
 * El comando "drawPixel" toma las coordenadas x, y y un color (representado por tres valores RGB) para dibujar un píxel en la pantalla.
 *
 * Ejemplo de comando:
 * <pre>
 * drawPixel&lt;350, 350, (255, 255, 255)&gt;
 * </pre>
 * Este comando dibuja un píxel blanco en las coordenadas (350, 350).
 */
public class DrawPixel extends Command {

    /**
     * Constructor de la clase {@link DrawPixel}.
     *
     * @param comamand El comando como una cadena de texto que se procesa para extraer los parámetros.
     * @param windows La ventana principal (JFrame) para realizar la actualización gráfica.
     */
    public DrawPixel(String comamand, JFrame windows) {
        super(comamand, windows);
    }

    /**
     * Ejecuta el comando "drawPixel", que dibuja un píxel en la pantalla en las coordenadas proporcionadas con el color especificado.
     * <p>
     * El formato del comando es "drawPixel&lt;x, y, (r, g, b)&gt;", donde:
     * - x: la coordenada horizontal.
     * - y: la coordenada vertical.
     * - r, g, b: los valores del color en formato RGB (0-255).
     * <p>
     * Ejemplo:
     * Si el comando recibido es:
     * <pre>
     * drawPixel&lt;350, 350, (255, 255, 255)&gt;
     * </pre>
     * Este método dibujará un píxel blanco en la posición (350, 350) en la pantalla.
     *
     * @return null
     */
    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        does_he_have_permissions(dataUser, this.getClass());
        if (this.id.equalsIgnoreCase("drawPixel")
                && does_he_have_permissions(dataUser, this.getClass())) {
            // drawPixel<350, 350, (255,255,255)>

            // clase de formateo de datos
            ParamFormatter param = new ParamFormatter(this.params);

            // pasa a string el primer argumento
            int x = param.asInt(0), y = param.asInt(1);

            // formatear el valor compuesto:
            param = new ParamFormatter(param.asList(2));
            System.out.println(param.asInt(0)+param.asInt(1)+ param.asInt(2));
            GUI.dotDrawer.drawPixel(new Color(
                   param.asInt(0), param.asInt(1), param.asInt(2)
            ), x, y);
            GUI.dotDrawer.repaint_all();

        }
        this.ret_client(out); // retornar
        return null;
    }
}
