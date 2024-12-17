package src;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUI extends JPanel {

    private static final Dimension TAMANO_PANTALLA = Toolkit.getDefaultToolkit().getScreenSize();

    static final int ALTURA = TAMANO_PANTALLA.height;
    static final int ANCHO = TAMANO_PANTALLA.width;


    public static GUI dotDrawer;
    public static JFrame frame;
    private BufferedImage buffer;
    public static RepaintThread update_thread;


    public GUI(){
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
        frame.setTitle("8086 Emulator");

        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dotDrawer = this;
        frame.add(this);

        frame.setPreferredSize(new Dimension(scaledWidth, scaledHeight));
        frame.pack(); // no usar setSize, no funciona

        frame.setVisible(true);
        repaint_all(); // Pinta el fondo negro al inicio
    }

    private void drawDot(Graphics g, Color self_color, int x, int y) {
        g.setColor(self_color); // Set the color of the dot
        g.fillRect(x, y, 1, 1); // Draw a single pixel
    }

    public void updatePixel(Color color, int x, int y) {
        SwingUtilities.invokeLater(() -> {
            drawPixel(color, x, y);
            repaint(x, y, 1, 1);
        });
    }

    public void repaint_all() {
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    public void drawPixel(Color color, int x, int y) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(buffer, 0, 0, this);
        g2d.dispose();
    }


}
