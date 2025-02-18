package src.Focus;

import java.awt.*;
import java.awt.image.BufferedImage;

import src.GUI;

public class InformationFocus {
    public int x, y;
    public int height, width;
    public Color color;

    // Buffer gráfico del foco
    public transient BufferedImage buffer;
    // Copia del área de fondo antes de dibujar el foco
    private transient BufferedImage backgroundBackup;

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
        // tal vez, no sea necesario
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); // Asegurar el tamaño correcto
        Graphics2D g = buffer.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height); // Dibujar el área del foco
        g.dispose();
    }

    /**
     * Método para guardar el área del fondo antes de dibujar el foco.
     * Se debe llamar antes de mover o redimensionar el foco.
     */
    public void backupBackground(BufferedImage guiBuffer) {
        // Se crea una copia profunda del área del guiBuffer que corresponde al foco
        backgroundBackup = new BufferedImage(width, height, guiBuffer.getType());
        Graphics2D bgG = backgroundBackup.createGraphics();
        bgG.drawImage(guiBuffer,
                0, 0, width, height,
                x, y, x + width, y + height,
                null);
        bgG.dispose();
    }

    /**
     * Restaura el área del fondo que se había guardado.
     */
    public void restoreBackground(BufferedImage guiBuffer) {
        if (backgroundBackup != null) {
            Graphics2D g2d = guiBuffer.createGraphics();
            g2d.drawImage(backgroundBackup, x, y, null);
            g2d.dispose();
        }
    }

    /**
     * Renderiza el foco en el buffer global. Antes de dibujar, restaura el área
     * donde estaba el foco (si se movió o redimensionó) para evitar que se acumule el dibujo.
     */
    public void render(Graphics2D g, BufferedImage guiBuffer) {
        // Primero restauramos el área antigua (si se ha guardado)
        restoreBackground(guiBuffer);
        // Luego, guardamos el área de fondo actual en la nueva posición
        backupBackground(guiBuffer);
        // Finalmente, dibujamos el foco en el guiBuffer en la posición (x,y)
        g.drawImage(buffer, x, y, null);
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
