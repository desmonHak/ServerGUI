package src;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import src.Focus.Focus;

/**
 * Clase que representa la interfaz gráfica del emulador 8086.
 * Esta clase extiende `JPanel` y se encarga de gestionar el repintado de la
 * pantalla, así como de la visualización del contenido gráfico mediante un
 * buffer de imagen.
 */
public class GUI
        extends JPanel
        implements KeyListener, MouseListener, MouseMotionListener {

    /** Dimensiones de la pantalla del dispositivo */
    static final Dimension TAMANO_PANTALLA = Toolkit.getDefaultToolkit().getScreenSize();

    /** Altura de la pantalla */
    static final int ALTURA = TAMANO_PANTALLA.height;

    /** Ancho de la pantalla */
    static final int ANCHO = TAMANO_PANTALLA.width;

    /** Ventana principal del emulador */
    public JFrame frame;

    /** Buffer de imagen para dibujar el contenido */
    private BufferedImage buffer;

    /** Hilo para actualizar la pantalla */
    public RepaintThread update_thread;

    public String BufferKeboard; // String donde almacenar todas las teclas pulsadas

    // velocidad de seguimiento del raton
    public float interpolationFactor = 0.9f;
    public final int radio = 4; // radio para el raton
    /* Posiciones del cursor del raton: */
    public Point targetPosition = new Point(0, 0);
    public Point currentPosition = new Point(0, 0);
    public boolean isMousePressed = false;
    public Color colorMouse = new Color(255, 0,0, 64); // color del clic

    public Focus foco;

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

        update_thread = new RepaintThread(true, this);
        // iniciar el hilo que repinta la ventana
        new Thread(update_thread).start();

        frame = new JFrame();

        //frame.setUndecorated(true);  // Elimina los bordes de la ventana

        frame.setLocation(ANCHO/4, ALTURA/4); //localizacion de la pantalla
        frame.setTitle("Titulo");

        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);

        frame.setPreferredSize(new Dimension(scaledWidth, scaledHeight));
        frame.pack(); // no usar setSize, no funciona

        /* Añadir el raton */
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        /* Añadir teclado: */
        this.addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);


        frame.setVisible(true);
        repaint_all(); // Pinta el fondo negro al inicioç


        this.foco = new Focus(this, 0, "root");
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

        // Dibujar cada foco dentro del buffer
        for (Focus focus : this.foco.stack_focus_root.values()) {
            focus.render(g2d, buffer);
        }

        // Interpolar la posición del círculo
        if (isMousePressed) {
            // Interpolar la posición del círculo
            float dx = targetPosition.x - currentPosition.x;
            float dy = targetPosition.y - currentPosition.y;
            currentPosition.x += dx * interpolationFactor;
            currentPosition.y += dy * interpolationFactor;

            // Dibujar el círculo interpolado
            g2d.setColor(colorMouse);
            g2d.fillOval((int)currentPosition.x - radio,
                    (int)currentPosition.y - radio, 2*radio, 2*radio);
        }

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

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    /* Cuando se pulsa el botón izquierdo del ratón se llama a mousePressed */
    @Override
    public void mousePressed(MouseEvent ev) {
        // lógica que cuando se presiona el botón del ratón
        isMousePressed = true;
        targetPosition.setLocation(ev.getPoint());
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /* Metodos de MouseMotionListener */
    @Override
    public void mouseMoved(MouseEvent e) {
        targetPosition.setLocation(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isMousePressed) {
            targetPosition.setLocation(e.getPoint());
        }
    }
}
