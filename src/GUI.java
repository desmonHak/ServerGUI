package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Clase que representa la interfaz gráfica del emulador 8086.
 * Esta clase extiende `JPanel` y se encarga de gestionar el repintado de la
 * pantalla, así como de la visualización del contenido gráfico mediante un
 * buffer de imagen.
 */
public class GUI extends JPanel implements KeyListener {

    /** Dimensiones de la pantalla del dispositivo */
    private static final Dimension TAMANO_PANTALLA = Toolkit.getDefaultToolkit().getScreenSize();

    /** Altura de la pantalla */
    static final int ALTURA = TAMANO_PANTALLA.height;

    /** Ancho de la pantalla */
    static final int ANCHO = TAMANO_PANTALLA.width;

    /** Instancia de la clase GUI, utilizada para repintar la interfaz */
    public static GUI dotDrawer;

    /** Ventana principal del emulador */
    public static JFrame frame;

    /** Buffer de imagen para dibujar el contenido */
    private BufferedImage buffer;

    /** Hilo para actualizar la pantalla */
    public static RepaintThread update_thread;

    public static String BufferKeboard; // String donde almacenar todas las teclas pulsadas

    /**
     * Constructor de la clase `GUI`. Inicializa la ventana y el buffer de imagen.
     * Calcula el tamaño de la ventana en función de la resolución de pantalla y
     * establece un fondo negro para la interfaz.
     */
    public GUI(){
        BufferKeboard = "";
        int dpi = Toolkit.getDefaultToolkit().getScreenResolution();

        double scaleFactor = dpi / 96.0; // 96 es el valor estándar de DPI

        // Ajusta el tamaño de la ventana en función del factor de escala
        int scaledWidth = (int) ((double) ANCHO / 2 * scaleFactor);
        int scaledHeight = (int) ((double) ALTURA / 2 * scaleFactor);

        buffer = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        // llenar el buffer con el color negro
        Graphics2D g2d = buffer.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, scaledWidth, scaledHeight);
        g2d.dispose();

        update_thread = new RepaintThread(false);

        frame = new JFrame();

        //frame.setUndecorated(true);  // Elimina los bordes de la ventana

        frame.setLocation(ANCHO/4, ALTURA/4); //localizacion de la pantalla
        frame.setTitle("Titulo");

        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dotDrawer = this;
        frame.add(this);

        frame.setPreferredSize(new Dimension(scaledWidth, scaledHeight));
        frame.pack(); // no usar setSize, no funciona

        /* Añadir teclado: */
        this.addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);


        frame.setVisible(true);
        repaint_all(); // Pinta el fondo negro al inicio
    }

    /**
     * Dibuja un píxel en la pantalla en la ubicación especificada con el color dado.
     *
     * @param g El objeto `Graphics` utilizado para dibujar.
     * @param self_color El color del píxel a dibujar.
     * @param x La coordenada X del píxel.
     * @param y La coordenada Y del píxel.
     */
    private void drawDot(Graphics g, Color self_color, int x, int y) {
        g.setColor(self_color); // Set the color of the dot
        g.fillRect(x, y, 1, 1); // Draw a single pixel
    }

    /**
     * Actualiza el color de un píxel en la pantalla.
     *
     * @param color El color que debe tener el píxel.
     * @param x La coordenada X del píxel.
     * @param y La coordenada Y del píxel.
     */
    public void updatePixel(Color color, int x, int y) {
        SwingUtilities.invokeLater(() -> {
            drawPixel(color, x, y);
            repaint(x, y, 1, 1);
        });
    }

    /**
     * Solicita el repintado completo de la pantalla.
     */
    public void repaint_all() {
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    /**
     * Dibuja un píxel en el buffer de imagen.
     *
     * @param color El color del píxel.
     * @param x La coordenada X del píxel.
     * @param y La coordenada Y del píxel.
     */
    public void drawPixel(Color color, int x, int y) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    /**
     * Sobrescribe el método `paintComponent` de `JPanel` para pintar el contenido
     * del buffer de imagen en el panel de la interfaz.
     *
     * @param g El objeto `Graphics` utilizado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(buffer, 0, 0, this);
        g2d.dispose();
    }

    /*
     * si la tecla es presionada una o permanentemente
     */
    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println("1 " + e.getKeyChar());
        BufferKeboard += e.getKeyChar();
        System.out.println(BufferKeboard);
    }
    /*

     * si la tecla es presionada permanentemente
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("2 " + e.getKeyChar());
    }

    /*
     * si la tecla es presionada y luego se libera, al iberarla, se ejecuta la funcion
     */
    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("3 " + e.getKeyChar());
    }
}
