package src.Commands;

import src.ACL.Groups;
import src.ACL.Users;
import src.Focus.Focus;
import src.Pair;

import javax.swing.*;
import java.io.PrintWriter;

public class GetNowFocus extends Command {
    public GetNowFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        if (this.id.equalsIgnoreCase("getNowFocus")
                && does_he_have_permissions(dataUser, this.getClass())) {
            // getNowFocus<>
            this.ret_client(out, "\"%s\"".formatted(Focus.now_Focus));
        }
        this.ret_client(out); // retornar
        return null;
    }
}
