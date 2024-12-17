package src.Commands;

import src.GUI;

import javax.swing.*;
import java.util.Objects;


public class SetTitle extends Command {
    public SetTitle(String comamand, JFrame windows) {
        super(comamand, windows);
    }

    @Override
    public Objects exec(){
        if (this.id.equalsIgnoreCase("setTitle")) {
            // setTitle<"hola mundo">

            // clase de formateo de datos
            ParamFormatter title = new ParamFormatter(this.params);

            // pasa a string el primer argumento
            GUI.frame.setTitle(title.asString(0));
        }
        return null;
    }
}
