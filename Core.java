import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import src.ClientHandler;

public class Core extends JPanel {
    static public Color[] datos;

    final private static Dimension tamanoPantalla = Toolkit.getDefaultToolkit().getScreenSize();
    static int altura = tamanoPantalla.height+100;
    static int ancho = tamanoPantalla.width+100;

    private ServerSocket serverSocket;
    ClientHandler clientHandler;

    private void drawDot(Graphics g, Color self_color, int x, int y) {
        g.setColor(self_color); // Set the color of the dot
        g.fillRect(x, y, 1, 1); // Draw a single pixel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = 0; x <= ancho/2; x++) {
            for (int y = 0; y <= altura/2; y++) {
                drawDot(g, datos[(x*y) % datos.length], x, y); // Drawing a pixel at (50, 50)
            }
        }
    }

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

    public static void main(String[] args) {
        datos = new Color[10];
        JFrame frame = new JFrame();
        Core dotDrawer = new Core();

        frame.add(dotDrawer);

        frame.setSize(ancho/2, altura/2); //tamano de la pantalla
        frame.setLocation(ancho/4, altura/4); //localizacion de la pantalla
        frame.setTitle("8086 Emulator");

        frame.setVisible(true);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dotDrawer.start(4500);
    }
}