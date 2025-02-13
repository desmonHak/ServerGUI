package src.Commands;

import java.io.PrintWriter;
import java.util.Objects;
import javax.swing.*;
import src.Focus.Focus;

public class GetAttribFocus extends Command {
    public GetAttribFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public Objects exec(PrintWriter out){
        if (this.id.equalsIgnoreCase("getAttribFocus")) {
            // getAttribFocus<>
            this.ret_client(out, 
                "\"%s\", %s".formatted(
                    Focus.now_Focus,
                    Focus.now_Focus.information
                )
            );
        }
        this.ret_client(out); // retornar
        return null;
    }
}
