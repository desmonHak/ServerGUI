package src.Commands;

import src.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class DrawPixel extends Command {
    public DrawPixel(String comamand, JFrame windows) {
        super(comamand, windows);
    }

    @Override
    public Objects exec(){
        if (this.id.equalsIgnoreCase("drawPixel")) {
            // drawPixel<350, 350, (255,255,255)>

            // clase de formateo de datos
            ParamFormatter param = new ParamFormatter(this.params);

            // pasa a string el primer argumento
            int x = param.asInt(0), y = param.asInt(1);

            // formatear el valor compuesto:
            param = new ParamFormatter(param.asList(2));
            System.out.println(param.asInt(0)+param.asInt(1)+ param.asInt(2));
            GUI.dotDrawer.drawPixel(new Color(
                   param.asInt(0), param.asInt(1), param.asInt(2)
            ), x, y);
            GUI.dotDrawer.repaint_all();

        }
        return null;
    }
}
