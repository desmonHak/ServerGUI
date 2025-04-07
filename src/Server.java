package src;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

import src.ACL.SerializableObjects;


/**
 * Clase que representa un servidor que acepta conexiones de clientes a través de un puerto.
 * El servidor escucha las solicitudes de conexión entrantes y las maneja en hilos separados.
 *
 *
 */
public class Server {
    private int port;

    // socket del servidor
    private ServerSocket serverSocket;

    // manejador del cliente actual:
    ClientHandler clientHandler;

    // pila de hilos, cada cliente conectado tendra su propio hilo:
    private ConcurrentHashMap<Thread, ClientHandler> stack_threads = new ConcurrentHashMap<>();

    // ACL = Acess Control List (lista de control de acceso)
    public SerializableObjects ACL;
    public GUI gui;
    /**
     * Instanciar un servidor
     *
     * @param port El puerto en el que el servidor escuchará las conexiones.
     */
    public Server(int port) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        ACL = new SerializableObjects(null);
        try {
            ACL.load_users_and_groups();
        } catch (FileNotFoundException e) {
            ACL.load_default_users_and_groups();
        }

        this.port = port;
        this.gui = new GUI();
    }

    /**
     * Instanciar un servidor
     *
     * @param port El puerto en el que el servidor escuchará las conexiones.
     */
    public Server(
            int port,
            String name_file
    ) throws IOException, ClassNotFoundException {
        ACL = new SerializableObjects(name_file);
        ACL.load_users_and_groups();

        this.port = port;
        this.gui = new GUI();
    }

    /**
     * Inicia el servidor y lo pone a escuchar en el puerto especificado.
     * Acepta conexiones entrantes de clientes y les asigna un {@link ClientHandler} en un hilo independiente.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            System.out.println("Servidor iniciado en el puerto: " + port);

            // Capturar Ctrl + C
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Interceptado Ctrl + C. Cerrando servidor...");
                stop();
                try {
                    ACL.write_users_and_groups();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));

            while (!serverSocket.isClosed()) {
                try {
                    Socket client = serverSocket.accept();
                    System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());
                    clientHandler = new ClientHandler(client, ACL, gui);

                    //clientHandler.setClientSocket(client); // añadir el socket

                    Thread hilo = new Thread(clientHandler);
                    stack_threads.put(hilo, clientHandler); // Guardar el hilo en la lista
                    hilo.start();

                } catch (IOException e) {
                    System.out.println("Error al aceptar la conexion: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Detiene el servidor cerrando el {@link ServerSocket} y liberando los recursos asociados.
     */
    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor detenido.");
            }
        } catch (IOException e) {
            System.out.println("Error al detener el servidor: " + e.getMessage());
        }
    }
}
