package src.Commands;

import java.io.PrintWriter;
import javax.swing.*;

import src.ACL.Groups;
import src.ACL.Users;
import src.Focus.Focus;
import src.Pair;

public class GetAttribFocus extends Command {
    public GetAttribFocus(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser){
        if (this.id.equalsIgnoreCase("getAttribFocus")
                && does_he_have_permissions(dataUser, this.getClass())) {
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
