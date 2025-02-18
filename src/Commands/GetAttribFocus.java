package src.Commands;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

import src.ACL.Groups;
import src.ACL.Users;
import src.Focus.Focus;
import src.Pair;

import static src.Focus.Focus.get_FocusFather;
import static src.Focus.Focus.stack_focus_root;

public class GetAttribFocus extends Command {
    public GetAttribFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        if (this.id.equalsIgnoreCase("getAttribFocus")
                && does_he_have_permissions(dataUser, this.getClass())) {
            first:
            {
                // getAttribFocus<>
                // getAttribFocus<"root.a.b">

                // clase de formateo de datos
                ParamFormatter title = new ParamFormatter(this.params);
                System.out.println(Arrays.toString(this.params.toArray()));
                if (this.params.isEmpty()) {
                    this.ret_client(out,
                            "\"%s\", %s".formatted(
                                    Focus.now_Focus,
                                    Focus.now_Focus.information
                            )
                    );
                } else {

                    // pasa a string el primer argumento
                    String name_focus = title.asString(0);

                    // obtener el foco actual
                    // getAttribFocus<"now">
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
                    this.ret_client(out,
                            "\"%s\", %s".formatted(
                                    f,
                                    f.information
                            )
                    );
                }
                return null;
            }
        }
        this.ret_client(out); // retornar
        return null;
    }
}
