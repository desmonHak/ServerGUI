package src.Commands;

import java.io.PrintWriter;
import java.util.Objects;
import javax.swing.*;
import src.Focus.*;

public class DeleteFocus extends Command {
    public DeleteFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public Objects exec(PrintWriter out) {
        if (this.id.equalsIgnoreCase("deleteFocus")) {
            // deleteFocus<"root.Foco1">

            // clase de formateo de datos
            ParamFormatter title = new ParamFormatter(this.params);

            // pasa a string el primer argumento
            String name_focus = title.asString(0);

            Focus.deleteFocusInStack(name_focus);

        }
        this.ret_client(out); // retornar
        return null;
    }
}
