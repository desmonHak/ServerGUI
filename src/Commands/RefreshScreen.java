package src.Commands;

import java.util.Objects;
import javax.swing.*;

public class RefreshScreen extends Command {
    public RefreshScreen(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public Objects exec() {
        if (this.id.equalsIgnoreCase("refreshScreen")) {
            try {
                Process process = Runtime.getRuntime().exec("xrefresh");
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

/*
 * Comando que usa xrefresh, redibuja las ventanas abiertas para actualizar pantalla
 */
