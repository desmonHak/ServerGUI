package src.Commands;

import src.ACL.Groups;
import src.ACL.Users;
import src.Focus.Focus;
import src.Pair;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;

import static src.Focus.Focus.get_FocusFather;
import static src.Focus.Focus.stack_focus_root;

public class SetSizeFocus extends Command{
    public SetSizeFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        if (this.id.equalsIgnoreCase("setSizeFocus")
                && does_he_have_permissions(dataUser, this.getClass())) {
            // setSizeFocus<"root.Juan.a.2", 23, 34>

            // clase de formateo de datos
            ParamFormatter title = new ParamFormatter(this.params);

            // pasa a string el primer argumento
            String name_focus = title.asString(0);

            int height = title.asInt(1);
            int width  = title.asInt(2);

            // obtener el foco actual
            // setSizeFocus<"now", 23, 34>
            Focus f;
            if (name_focus.equals("now")) { f = Focus.now_Focus; }
            else {
                f = get_FocusFather(stack_focus_root, name_focus.split("\\."));
            }
            f.information.height = height;
            f.information.width = width;

            // es necesario hacer un buffer con el nuevo tamaño, sino no se vera reflejado
            f.information.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            f.information.paint();

            this.ret_client(out, "%s".formatted(true));
        }
        this.ret_client(out); // retornar
        return null;
    }
}
