package src;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import src.ClientHandler;
import javax.swing.*;

import static src.GUI.frame;

public class Server {
    private ServerSocket serverSocket;
    ClientHandler clientHandler;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            System.out.println("Servidor iniciado en el puerto: " + port);

            while (!serverSocket.isClosed()) {
                try {
                    Socket client = serverSocket.accept();
                    System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());
                    clientHandler = new ClientHandler(client);

                    //clientHandler.setClientSocket(client); // añadir el socket
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    System.out.println("Error al aceptar la conexión: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
            System.exit(1);
        }
    }

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
