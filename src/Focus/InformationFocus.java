package src.Focus;

import java.awt.*;
import java.awt.image.BufferedImage;

import src.GUI;

public class InformationFocus {
    public int x, y;
    public int height, width;
    public Color color;

    // Buffer gráfico del foco
    protected transient BufferedImage buffer;

    public InformationFocus(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.height = 10;
        this.width = 10;
        this.color = color;
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        paint(); // pintar
        GUI.dotDrawer.repaint_all(); // repintar la ventana
    }
    // Método para obtener el buffer del foco
    public BufferedImage getBuffer() {
        return buffer;
    }
    // Método para pintar dentro del buffer del foco
    public void paint() { // pintar dentro del buffer local del foco
        Graphics2D g = buffer.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height); // Dibujar el área del foco
        g.dispose();
    }

    // Método para renderizar el buffer en el buffer global de GUI
    public void render(Graphics2D g, BufferedImage guiBuffer) {
        Graphics2D g2d = (Graphics2D) guiBuffer.getGraphics();
        g2d.drawImage(buffer, x, y, null);
        g2d.dispose();
    }

    @Override
    public String toString() {
        return "\"%d x %d\", %d, %d, (%d, %d, %d)".formatted(
            x, y, 
            height, width,
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            color.getAlpha()
        );
    }

}
