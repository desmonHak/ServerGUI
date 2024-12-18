package src.Commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es responsable de formatear y convertir los parámetros recibidos como cadenas de texto
 * en tipos de datos apropiados, como cadenas, enteros, booleanos, flotantes y listas.
 *
 * Se utiliza para facilitar la extracción y conversión de parámetros de los comandos.
 *
 * Ejemplos:
 * - Si el parámetro es una cadena entre comillas (por ejemplo, `"texto"`), se devolverá `"texto"`.
 * - Si el parámetro es un número (por ejemplo, `42`), se convertirá a un entero.
 * - Si el parámetro es una lista de valores separados por comas y rodeados por paréntesis (por ejemplo, `(valor1, valor2)`), se convertirá en una lista de cadenas.
 */
public class ParamFormatter {

    /** Lista de parámetros recibidos */
    private final List<String> params;

    /**
     * Constructor que recibe una lista de parámetros.
     *
     * @param params Lista de parámetros a ser formateados y convertidos.
     */
    ParamFormatter(List<String> params) {
        this.params = params;
    }

    /**
     * Convierte un parámetro en un valor de tipo {@link String}.
     * Si el parámetro está rodeado por comillas, se elimina las comillas.
     *
     * Ejemplo:
     * <pre>
     * ParamFormatter formatter = new ParamFormatter(List.of("\"Hello World\""));
     * String result = formatter.asString(0);  // result es "Hello World"
     * </pre>
     *
     * @param index El índice del parámetro a convertir.
     * @return El parámetro como una cadena de texto.
     */
    public String asString(int index) {
        String param = params.get(index);
        if (param.startsWith("\"") && param.endsWith("\"")) {
            return param.substring(1, param.length() - 1);
        }
        return param;
    }

    /**
     * Convierte un parámetro en un valor de tipo {@link Integer}.
     *
     * Ejemplo:
     * <pre>
     * ParamFormatter formatter = new ParamFormatter(List.of("42"));
     * int result = formatter.asInt(0);  // result es 42
     * </pre>
     *
     * @param index El índice del parámetro a convertir.
     * @return El parámetro como un entero.
     */
    public int asInt(int index) {
        return Integer.parseInt(params.get(index));
    }

    /**
     * Convierte un parámetro en un valor de tipo {@link Boolean}.
     *
     * Ejemplo:
     * <pre>
     * ParamFormatter formatter = new ParamFormatter(List.of("true"));
     * boolean result = formatter.asBoolean(0);  // result es true
     * </pre>
     *
     * @param index El índice del parámetro a convertir.
     * @return El parámetro como un valor booleano.
     */
    public boolean asBoolean(int index) {
        return Boolean.parseBoolean(params.get(index));
    }

    /**
     * Convierte un parámetro en un valor de tipo {@link Float}.
     *
     * Ejemplo:
     * <pre>
     * ParamFormatter formatter = new ParamFormatter(List.of("3.14"));
     * float result = formatter.asFloat(0);  // result es 3.14
     * </pre>
     *
     * @param index El índice del parámetro a convertir.
     * @return El parámetro como un valor flotante.
     */
    public float asFloat(int index) {
        return Float.parseFloat(params.get(index));
    }

    /**
     * Convierte un parámetro en una lista de cadenas.
     * El parámetro debe estar rodeado por paréntesis y los elementos deben estar separados por comas.
     *
     * Ejemplo:
     * <pre>
     * ParamFormatter formatter = new ParamFormatter(List.of("(item1, item2, item3)"));
     * ArrayList&lt;String&gt; result = formatter.asList(0);  // result es ["item1", "item2", "item3"]
     * </pre>
     *
     * @param index El índice del parámetro a convertir.
     * @return Una lista de cadenas representando los elementos separados por comas.
     */
    public ArrayList<String> asList(int index) {
        String param = params.get(index).replaceAll("[()]", ""); // Eliminar paréntesis
        String[] parts = param.split(","); // Dividir por comas
        ArrayList<String> result = new ArrayList<>();
        for (String part : parts) {
            result.add(part.trim());
        }
        return result;
    }
}
