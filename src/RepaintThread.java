package src;

/**
 * Clase que maneja la actualización del contenido gráfico de la interfaz
 * de usuario. Esta clase implementa la interfaz `Runnable` para ejecutar
 * el repintado de la pantalla en un hilo separado. Su principal función es
 * repintar la interfaz a una tasa constante (aproximadamente 60 FPS) si
 * el estado de la variable `estatus` es verdadero.
 */
public class RepaintThread  implements Runnable {

    /**
     * Indica si la pantalla debe actualizarse o no. Si el estado es `true`,
     * la pantalla se repintará a intervalos regulares.
     */
    private boolean estatus; // indica si actualiza o no la pantalla

    /**
     * Constructor de la clase `RepaintThread`.
     *
     * @param estatus Estado inicial de la variable `estatus`. Si es `true`,
     *                el hilo comenzará a repintar la pantalla a intervalos regulares.
     */
    RepaintThread(boolean estatus) {
        this.estatus = estatus;
    }

    /**
     * Obtiene el estado actual del repintado.
     *
     * @return El estado de repintado. `true` si la pantalla debe actualizarse,
     *         `false` si no.
     */
    public boolean isEstatus() {
        return estatus;
    }

    /**
     * Establece el estado del repintado.
     *
     * @param estatus Nuevo estado de repintado. Si es `true`, el repintado se
     *                activará; si es `false`, el repintado se desactivará.
     */
    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    /**
     * Método que ejecuta el ciclo de repintado en un hilo separado. Si el
     * estado de `estatus` es `true`, el hilo repinta la pantalla cada 16 ms
     * aproximadamente (lo que corresponde a 60 FPS). Si el estado es `false`,
     * el repintado no ocurre.
     */
    @Override
    public void run() {
        while (true) {
            if (estatus) {
                try {
                    GUI.dotDrawer.repaint_all(); // Llama al repintado genera
                } catch (NullPointerException e) {
                    System.out.println("dotDrawer aun no fue instanciada");
                }
                try {
                    Thread.sleep(16); // 60 FPS aproximadamente
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
