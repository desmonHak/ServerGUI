package src;

public class RepaintThread  implements Runnable{
    private boolean estatus; // indica si actualiza o no la pantalla

    RepaintThread(boolean estatus) {
        this.estatus = estatus;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    @Override
    public void run() {
        while (true) {
            if (estatus) {
                GUI.dotDrawer.repaint_all(); // Llama al repintado general
                try {
                    Thread.sleep(16); // 60 FPS aproximadamente
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
