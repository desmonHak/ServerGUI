import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import src.ACL.Users;
import src.Server;

/**
 * Clase principal que inicializa el servidor y la interfaz gráfica del emulador.
 * Esta clase contiene el punto de entrada para la aplicación, creando una instancia
 * del servidor y la GUI, y luego iniciando el servidor en un puerto específico.
 */
public class Core  {
    /**
     * Metodo principal que se ejecuta al iniciar la aplicación.
     * Crea una instancia del servidor y la interfaz gráfica, y luego inicia el servidor
     * en el puerto 450.
     *
     * @param args Argumentos de línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(
            String[] args
    ) throws IOException,
            ClassNotFoundException,
            NoSuchAlgorithmException
    {

        URL packageURL = Users.class.getClassLoader().getResource("src/Commands");
        System.out.println("Buscando clases comando en: " + packageURL);

        Server server = new Server(450);

        server.start();
    }
}