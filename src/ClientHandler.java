package src;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.swing.*;

import src.ACL.Groups;
import src.ACL.SerializableObjects;
import src.ACL.Users;
import src.Commands.*;
import src.Errors.PasswordError;

import static src.ACL.Users.CLASS_EXCLUIR;
import static src.ACL.Users.getClassesInPackage;

/**
 * Clase encargada de manejar la comunicación con un cliente conectado al servidor.
 * Recibe las solicitudes del cliente, las procesa, ejecuta los comandos correspondientes
 * y envía respuestas de vuelta al cliente.
 */
public class ClientHandler implements Runnable {

    /** Socket de comunicación con el cliente */
    public Socket clientSocket;

    /** Flujo de entrada de datos desde el cliente */
    private InputStream in;

    /** Flujo de salida de datos hacia el cliente */
    public PrintWriter out;

    /** Ventana de la interfaz gráfica */
    private JFrame windows;

    /**
     * El contador de mensajes nulos permite cerrar la conexion a los clientes que envian
     * peticiones sin datos. Puede ocurrir que el cliente se desconecte sin notificar al servidor.
     * El servidor entrara en un bucle indefinido recibiendo ningun tipo de datos, limitando este numero
     * podemos cerrar este handler.
     */
    private  int counter_null_msj     = 0;

    /** Número máximo de mensajes nulos permitidos antes de cerrar la conexión */
    public   int counter_null_msj_max = 3;

    /** Mapa que asocia los identificadores de comandos con sus clases correspondientes */
    private HashMap<String, Class<? extends Command>> commandMap = new HashMap<>();

    /** Lista de acceso con ususarios y grupos con sus respectivos permisos */
    private SerializableObjects ACL;

    // usuario y grupo del cliente actual, si su session es valida, se tiene el grupo y usuario:
    Pair<Groups, Users> data_user;

    public GUI gui;

    /**
     * Constructor de la clase `ClientHandler`.
     *
     * @param socket El socket de conexión con el cliente.
     * @param counter_null_msj_max El número máximo de mensajes nulos permitidos.
     */
    public ClientHandler(
            Socket socket,
            int counter_null_msj_max,
            SerializableObjects ACL,
            GUI gui
    ) throws ClassNotFoundException {
        this.clientSocket           = socket;
        this.counter_null_msj_max   = counter_null_msj_max;
        this.commandMap             = initializeCommandMap();
        this.ACL                    = ACL;
        this.gui = gui;
    }

    /**
     * Constructor de la clase `ClientHandler`.
     *
     * @param socket El socket de conexión con el cliente.
     */
    public ClientHandler(
            Socket socket,
            SerializableObjects ACL,
            GUI gui
    ) throws ClassNotFoundException {
        this.clientSocket           = socket;
        this.gui = gui;
        this.windows                = this.gui.frame;
        this.commandMap             = initializeCommandMap();
        this.ACL                    = ACL;
    }

    /**
     * Inicializa el mapa de comandos que asocia los identificadores con las clases
     * de los comandos correspondientes.
     *
     * @return El mapa de comandos.
     */
    private HashMap <String, Class<? extends Command>>initializeCommandMap() throws ClassNotFoundException {
        HashMap<String, Class<? extends Command>> map = new HashMap<>();
        // comandos:
        /*map.put("setTitle",         SetTitle.class);
        map.put("drawPixel",        DrawPixel.class);
        map.put("autoUpdateScreen", AutoRefreshScreen.class);
        map.put("getKeyboard",      GetKeyboard.class);
        map.put("resetKeyboard",    ResetKeyboard.class);
        map.put("createFocus",      CreateFocus.class);
        map.put("deleteFocus",      DeleteFocus.class);
        map.put("getNowFocus",      GetNowFocus.class);
        map.put("getAttribFocus",   GetAttribFocus.class);*/


        try {
            // obtener un set con todas clases de la carpeta comandos, ignorando las clases indicadas en CLASS_EXCLUIR :
            Set<Class<?>> commandClasses = getClassesInPackage("src.Commands", CLASS_EXCLUIR);
            for (Class<?> clazz : commandClasses) {
                // Verificar que la clase es una subclase de Command antes de agregarla al HashMap
                if (Command.class.isAssignableFrom(clazz) && !clazz.equals(Command.class)) {
                    //System.out.println("Clase encontrada: " + clazz.getName());
                    String name_command = clazz.getSimpleName();
                    map.put(
                            Character.toLowerCase(name_command.charAt(0)) + // string del comando (nombre del comando)
                                    name_command.substring(1),
                            (Class<? extends Command>) clazz // clase que se ejecutara
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private StringBuilder read_line(InputStream in) throws IOException {
        StringBuilder request = new StringBuilder();
        int data;
        while ((data = in.read()) != -1) {
            // Leer byte por byte y construir la solicitud
            request.append((char) data);
            // Si llega un salto de línea (\n), procesamos la línea
            if (data == '\n') {
                break;
            }
        }
        return request;
    }

    /**
     * Metodo principal que maneja la comunicación con el cliente.
     * Lee los mensajes del cliente, los procesa y responde con el resultado o un error.
     */
    @Override
    public void run() {
        try {
            in = clientSocket.getInputStream();
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // mientras el cliente no diga que la conexion finalizo, seguir con ella
            boolean life = true;

            out.println("username: ");
            String username = read_line(in).toString().trim();
            data_user = Groups.searchUserNameInGroup(ACL.groups, username);
            if (data_user == null) {
                life = false;
                out.println("el usuario no existe");
            } else {
                out.println("<%s>password: ".formatted(username));
                String password = read_line(in).toString().trim();

                Users user = data_user.getSecond();
                try {
                    // verificar si las credenciales son correctas:
                    Users credentials = new Users(username, password, user.hash_pass, user.group);
                } catch (PasswordError e) {
                    life = false;
                    System.out.println("Usuario %s del group %s con hash %s intento acceder con las credenciales %s".
                            formatted(
                                username, data_user.getFirst().name, user.hash_pass, password
                    ));
                }
            }


            while(life) {
                StringBuilder request = read_line(in);

                // si no se envio informacion, aumentar el contador de mensajes nulos
                String response = null;
                if (request.isEmpty()) {
                    counter_null_msj++;
                    response = "\n";
                } else {
                    counter_null_msj = 0;
                    // Enviar respuesta al cliente
                    response = processRequest(request.toString());
                }
                if (response != null) { // si se obtuvo una respuesta a enviar
                    out.println(response);
                    if (response.equalsIgnoreCase("exit")){
                        life = false;
                    }
                }
                if (counter_null_msj == this.counter_null_msj_max) {
                    life = false;
                    continue;
                }

                // Mostrar la solicitud recibida
                System.out.println("Solicitud recibida " + request.length() + " : \n" + request);
            }

        } catch (IOException e) {
            System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket del cliente: " + e.getMessage());
            }
        }
        System.out.println("Cliente cerrado " + clientSocket);
    }


    /**
     * Procesa la solicitud recibida, identificando el comando y ejecutándolo.
     * Si el comando no es válido, retorna un mensaje de error.
     *
     * @param request La solicitud recibida desde el cliente.
     * @return La respuesta al cliente, que puede ser el resultado del comando o un mensaje de error.
     */
    private String processRequest(String request) {
        String data_client = null;
        if (request.isEmpty()) return "";

        Command command = new Command(request, this.gui);
        System.out.println(request);

        if (command.id != null) {
            /*
             * Class representa la metainformación sobre una clase en tiempo de ejecución. Al indicar extend Command
             * Se aplica una "restrincion de tipo generico", solo se podría obtener tipos que extiendan de Command.
             * Con Class<? extends Command> podemos almacenar referencias a clases que heredan de Command, usar
             * Class<Command> solo permitiría almacenar objetos de la clase Command.
             */
            Class<? extends Command> commandClass = commandMap.get(command.id);

            if (commandClass != null) {
                try {
                    /*
                     * crear una instancia de la clase especificada usando relfexion:
                     *      String.class: La solicitud completa (request)
                     *      JFrame.class: El objeto windows (la referencia a la ventana gráfica, en este caso).
                     */
                    Command specificCommand = commandClass.getDeclaredConstructor(String.class, GUI.class)
                            .newInstance(request, this.gui);
                    // llamar al metodo exec del objeto:
                    data_client = specificCommand.exec(out, data_user);

                    // si el comando no devuelve null, quiere decir que se debe enviar los datos
                    //specificCommand.ret_client(out, (String) data_client);
                    return data_client;

                } catch (Exception e) {
                    e.printStackTrace();
                    return "return[%s]<Error executing command, %s>".formatted(command.id, e.getMessage());
                }
            } else {
                return "return[%s]<Command not found>".formatted(command.id);
            }
        } else {
            return "return[%s]<Command is null>".formatted((Object)null);
        }
    }
}