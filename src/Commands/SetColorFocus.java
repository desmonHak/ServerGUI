package src.Commands;

import src.ACL.Groups;
import src.ACL.Users;
import src.Focus.Focus;
import src.GUI;
import src.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;

import static src.Focus.Focus.get_FocusFather;
import static src.Focus.Focus.stack_focus_root;

public class SetColorFocus extends Command {
    public SetColorFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser) {
        if (this.id.equalsIgnoreCase("setColorFocus")
                && does_he_have_permissions(dataUser, this.getClass())) {
            first:
            {

                // setColorFocus<"root.Juan.a.2", (255, 0, 0, 128)) // Rojo semi-transparente

                // clase de formateo de datos
                ParamFormatter param = new ParamFormatter(this.params);

                // pasa a string el primer argumento
                String name_focus = param.asString(0);

                // formatear el valor compuesto:
                param = new ParamFormatter(param.asList(1));
                System.out.println(
                        param.asInt(0) + param.asInt(1) + param.asInt(2) + param.asInt(3));
                Color alpha = new Color(
                        param.asInt(0), // red
                        param.asInt(1), // green
                        param.asInt(2), // blue
                        param.asInt(3)  // alpha
                );

                // obtener el foco actual
                // setColorFocus<"now", (255, 0, 0, 128)>
                Focus f;
                if (name_focus.equals("now")) {
                    f = Focus.now_Focus;
                    name_focus = f.name_focus;
                } else {
                    String[] name = name_focus.split("\\.");
                    f = get_FocusFather(stack_focus_root, name);
                    if (f == null) {
                        break first;
                    }
                    f = f.stack_focus_local.get(Focus.getLastNameFocus(name));
                }
                // si el foco no existe. retornar false
                if (f == null) {
                    break first;
                }
                // f.name_focus -> Nombre del nodo actual
                // name_focus -> ruta y nombre del foco
                System.out.println(f.name_focus + " " + name_focus);
                f.information.color = alpha;
                f.information.paint();
                Focus.now_Focus = f;

                this.ret_client(out, "%s".formatted(true));
                return null;
            }
        }
        this.ret_client(out, "%s".formatted(false));
        return null;
    }
}
