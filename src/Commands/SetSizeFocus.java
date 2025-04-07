package src.Commands;

import src.ACL.Groups;
import src.ACL.Users;
import src.Focus.Focus;
import src.GUI;
import src.Pair;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;

import static src.Focus.Focus.get_FocusFather;

public class SetSizeFocus extends Command{
    public SetSizeFocus(String command, GUI gui) {
        super(command, gui);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        if (this.id.equalsIgnoreCase("setSizeFocus")
                && does_he_have_permissions(dataUser, this.getClass())) {
            first:
            {
                // setSizeFocus<"root.Juan.a.2", 23, 34>

                // clase de formateo de datos
                ParamFormatter title = new ParamFormatter(this.params);

                // pasa a string el primer argumento
                String name_focus = title.asString(0);

                int height = title.asInt(1);
                int width = title.asInt(2);

                // obtener el foco actual
                // setSizeFocus<"now", 23, 34>
                Focus f;
                if (name_focus.equals("now")) {
                    f = Focus.now_Focus;
                    name_focus = f.name_focus;
                } else {
                    String[] name = name_focus.split("\\.");
                    f = get_FocusFather(Focus.stack_focus_root, name);
                    f = f.stack_focus_local.get(Focus.getLastNameFocus(name));
                }
                // si el foco no existe. retornar false
                if (f == null) {
                    break first;
                }
                // f.name_focus -> Nombre del nodo actual
                // name_focus -> ruta y nombre del foco
                System.out.println(f.name_focus + " " + name_focus);
                f.information.height = height;
                f.information.width = width;

                // es necesario hacer un buffer con el nuevo tamaño, sino no se vera reflejado
                f.information.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
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
