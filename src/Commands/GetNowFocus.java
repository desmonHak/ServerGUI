package src.Commands;

import src.GUI;
import src.Focus.Focus;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;

public class GetNowFocus extends Command {
    public GetNowFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public Objects exec(PrintWriter out){
        if (this.id.equalsIgnoreCase("getNowFocus")) {
            // getNowFocus<>
            this.ret_client(out, "\"%s\"".formatted(Focus.now_Focus));
        }
        this.ret_client(out); // retornar
        return null;
    }
}
