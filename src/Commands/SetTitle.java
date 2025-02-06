package src.Commands;

import src.GUI;

import javax.swing.*;
import java.io.PrintWriter;
import java.util.Objects;


public class SetTitle extends Command {
    public SetTitle(String comamand, JFrame windows) {
        super(comamand, windows);
    }

    @Override
    public Objects exec(PrintWriter out){
        if (this.id.equalsIgnoreCase("setTitle")) {
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
