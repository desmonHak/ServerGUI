package src.Commands;

import src.ACL.Groups;
import src.ACL.Users;
import src.Pair;

import javax.swing.*;
import java.io.PrintWriter;
import java.util.Map;

public class GetMyPermissions extends Command {
    public GetMyPermissions(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser) {
        if (this.id.equalsIgnoreCase("getMyPermissions")
                && does_he_have_permissions(dataUser, this.getClass())) {
            // getMyPermissions<>

            Groups MyGroup = dataUser.getFirst();
            Users  MyUser  = dataUser.getSecond();

            String data_ret = "";
            for (Map.Entry<Class<? extends Command>, Boolean> entry : MyUser.commands_permission.entrySet()) {
                data_ret += "\"%s\" : %s, ".formatted(entry.getKey().getSimpleName(), entry.getValue());
            }
            this.ret_client(out, data_ret);

        }
        this.ret_client(out); // retornar
        return null;
    }
}
