package src.Commands;

import java.io.PrintWriter;
import java.util.Objects;
import javax.swing.*;
import src.Focus.*;

public class CreateFocus extends Command {
    public CreateFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public Objects exec(PrintWriter out) {
        if (this.id.equalsIgnoreCase("createFocus")) {
            // createFocus<"root.Foco1", 1, >

            // clase de formateo de datos
            ParamFormatter title = new ParamFormatter(this.params);

            // pasa a string el primer argumento
            String name_focus = title.asString(0);

            int id_focus = title.asInt(1);

            Focus f = new Focus(id_focus, name_focus);


        }
        this.ret_client(out); // retornar
        return null;
    }
}
