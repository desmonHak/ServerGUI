package src.Commands;

import src.GUI;

import java.io.PrintWriter;
import java.util.Objects;
import javax.swing.*;

public class CreateFocus extends Command {
    public CreateFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public Objects exec(PrintWriter out) {
        if (this.id.equalsIgnoreCase("refreshScreen")) {
            try {
                Process process = Runtime.getRuntime().exec("xrefresh");
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.ret_client(out); // retornar
        return null;
    }
}

/*
 * Comando que usa xrefresh, redibuja las ventanas abiertas para actualizar pantalla
 */
