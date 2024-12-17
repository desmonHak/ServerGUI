package src.Commands;

import java.util.Objects;
import javax.swing.*;

public class AutoRefreshScreen extends Command {

    public AutoRefreshScreen(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public Objects exec() {
        if (this.id.equalsIgnoreCase("autoUpdateScreen")) {
            try {
                Process process = Runtime.getRuntime().exec("xdotool search --name 'Nombre de tu ventana' windowmap");
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
