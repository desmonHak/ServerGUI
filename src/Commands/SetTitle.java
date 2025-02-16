package src.Commands;

import src.ACL.Groups;
import src.ACL.Users;
import src.GUI;
import src.Pair;

import javax.swing.*;
import java.io.PrintWriter;


public class SetTitle extends Command {
    public SetTitle(String comamand, JFrame windows) {
        super(comamand, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        if (this.id.equalsIgnoreCase("setTitle")
                && does_he_have_permissions(dataUser, this.getClass())) {
            // setTitle<"hola mundo">

            // clase de formateo de datos
            ParamFormatter title = new ParamFormatter(this.params);

            // pasa a string el primer argumento
            GUI.frame.setTitle(title.asString(0));
        }
        this.ret_client(out); // retornar
        return null;
    }
}
