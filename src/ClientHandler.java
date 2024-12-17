package src;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private InputStream in;
    private PrintWriter out;

    /*
     * El contador de mensajes nulos permite cerrar la conexion a los clientes que envian
     * peticiones sin datos. Puede ocurrir que el cliente se desconecte sin notificar al servidor.
     * El servidor entrara en un bucle indefinido recibiendo ningun tipo de datos, limitando este numero
     * podemos cerrar este handler.
     */
    private  int counter_null_msj     = 0;
    public   int counter_null_msj_max = 3; // numero maximo de peticiones nulas a permitir


    public ClientHandler(Socket socket, int counter_null_msj_max) {
        this.clientSocket         = socket;
        this.counter_null_msj_max = counter_null_msj_max;
    }
    public ClientHandler(Socket socket){
        this.clientSocket = socket;
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
                if (request.isEmpty()) {
                    counter_null_msj++;
                } else {
                    counter_null_msj = 0;
                }

                // Enviar respuesta al cliente
                String response = processRequest(request.toString());
                out.println(response);
                if (response.equalsIgnoreCase("exit")){
                    leave = false;
                }
                if (counter_null_msj == this.counter_null_msj_max) {
                    leave = false;
                    break;
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
        System.out.println(request);
        return request;
    }

}