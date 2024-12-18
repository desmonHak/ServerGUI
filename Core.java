import src.GUI;
import src.Server;

/**
 * Clase principal que inicializa el servidor y la interfaz gráfica del emulador.
 * Esta clase contiene el punto de entrada para la aplicación, creando una instancia
 * del servidor y la GUI, y luego iniciando el servidor en un puerto específico.
 */
public class Core  {
    /**
     * Método principal que se ejecuta al iniciar la aplicación.
     * Crea una instancia del servidor y la interfaz gráfica, y luego inicia el servidor
     * en el puerto 450.
     *
     * @param args Argumentos de línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {
        Server server = new Server();
        GUI gui = new GUI();
        server.start(450);
    }
}