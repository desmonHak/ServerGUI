package src;

import src.Commands.AutoRefreshScreen;
import src.Commands.Command;
import src.Commands.DrawPixel;
import src.Commands.SetTitle;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private InputStream in;
    private PrintWriter out;
    private JFrame windows;
    /*
     * El contador de mensajes nulos permite cerrar la conexion a los clientes que envian
     * peticiones sin datos. Puede ocurrir que el cliente se desconecte sin notificar al servidor.
     * El servidor entrara en un bucle indefinido recibiendo ningun tipo de datos, limitando este numero
     * podemos cerrar este handler.
     */
    private  int counter_null_msj     = 0;
    public   int counter_null_msj_max = 3; // numero maximo de peticiones nulas a permitir

    private HashMap<String, Class<? extends Command>> commandMap = new HashMap<>();

    public ClientHandler(Socket socket, int counter_null_msj_max) {
        this.clientSocket           = socket;
        this.counter_null_msj_max   = counter_null_msj_max;
        this.commandMap             = initializeCommandMap();
    }
    public ClientHandler(Socket socket) {
        this.clientSocket           = socket;
        this.windows                = GUI.frame;
        this.commandMap             = initializeCommandMap();
    }

    private HashMap<String, Class<? extends Command>> initializeCommandMap() {
        HashMap<String, Class<? extends Command>> map = new HashMap<>();
        // comandos:
        map.put("setTitle", SetTitle.class);
        map.put("drawPixel", DrawPixel.class);
        map.put("autoUpdateScreen", AutoRefreshScreen.class);
        return map;
    }


    @Override
    public void run() {
        try {
            in = clientSocket.getInputStream();
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // mientras el cliente no dija que la conexion finalizo, seguir con ella
            boolean leave = true;
            while(leave) {

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

                // si no se envio informacion, aumentar el contador de mensajes nulos
                String response;
                if (request.isEmpty()) {
                    counter_null_msj++;
                    response = "\n";
                } else {
                    counter_null_msj = 0;
                    // Enviar respuesta al cliente
                    response = processRequest(request.toString());
                }
                out.println(response);

                if (response.equalsIgnoreCase("exit")){
                    leave = false;
                }
                if (counter_null_msj == this.counter_null_msj_max) {
                    leave = false;
                    continue;
                }

                // Mostrar la solicitud recibida
                System.out.println("Solicitud recibida " + request.length() + " : \n" + request);
            }

        } catch (IOException e) {
            System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
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



    private String processRequest(String request) {
        if (request.isEmpty()) return "";

        Command command = new Command(request, this.windows);
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
                    Command specificCommand = commandClass.getDeclaredConstructor(String.class, JFrame.class)
                            .newInstance(request, this.windows);
                    // llamar al metodo exec del objeto:
                    specificCommand.exec();
                } catch (Exception e) {
                    return "return[%s]<Error executing command, %s>".formatted(command.id, e.getMessage());
                }
            } else {
                return "return[%s]<Command not found>".formatted(command.id);
            }
        } else {
            return "return[%s]<Command is null>".formatted((Object)null);
        }

        return request;
    }

}