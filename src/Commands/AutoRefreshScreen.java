package src.Commands;

import src.GUI;

import java.util.Objects;
import javax.swing.*;

public class AutoRefreshScreen extends Command {

    public AutoRefreshScreen(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public Objects exec() {
        if (this.id.equalsIgnoreCase("autoUpdateScreen")) {
            ParamFormatter param = new ParamFormatter(this.params);
            GUI.update_thread.setEstatus(param.asBoolean(0));
        }
        return null;
    }
}
