package src;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUI extends JPanel {

    final private static Dimension tamanoPantalla = Toolkit.getDefaultToolkit().getScreenSize();

    static int altura = tamanoPantalla.height+100;
    static int ancho = tamanoPantalla.width+100;

    public static GUI dotDrawer;
    public static JFrame frame;
    private BufferedImage buffer;


    public GUI(){
        buffer = new BufferedImage(ancho/2, altura/2, BufferedImage.TYPE_INT_ARGB);

        frame = new JFrame();

        frame.setSize(ancho/2, altura/2); //tamano de la pantalla
        frame.setLocation(ancho/4, altura/4); //localizacion de la pantalla
        frame.setTitle("8086 Emulator");

        frame.setVisible(true);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dotDrawer = this;
        frame.add(this);
    }

    private void drawDot(Graphics g, Color self_color, int x, int y) {
        g.setColor(self_color); // Set the color of the dot
        g.fillRect(x, y, 1, 1); // Draw a single pixel
    }

    public void updatePixel(Color color, int x, int y) {
        this.drawPixel(color, x, y);
        repaint(x, y, 1, 1); // Solicita repintar solo el área del pixel
    }
    public void repaint_all() {
        repaint();
    }
    public void drawPixel(Color color, int x, int y) {
        if (x >= 0 && x < ancho/2 && y >= 0 && y < altura/2) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, this);
    }

}
