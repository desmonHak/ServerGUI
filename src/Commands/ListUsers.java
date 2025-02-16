package src.Commands;

import src.ACL.Groups;
import src.ACL.SerializableObjects;
import src.ACL.Users;
import src.Pair;

import javax.swing.*;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

public class ListUsers extends Command{
    public ListUsers(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser) {
        if (this.id.equalsIgnoreCase("listUsers")
                && does_he_have_permissions(dataUser, this.getClass())) {
            // listUsers<"root">

            // clase de formateo de datos
            ParamFormatter param = new ParamFormatter(this.params);

            // pasa a string el primer argumento
            String
                    s_group_name = param.asString(0);

            for (Groups group: SerializableObjects.groups) { // buscar si el grupo existe
                if(group.name.equals(s_group_name)){
                    String all_users = "";
                    for (Users user: group.users) {
                        all_users += "\"%s:%s\", ".formatted(user.name_user, user.hash_pass);
                    }
                    this.ret_client(out, all_users);
                }
            }
        }
        this.ret_client(out); // retornar
        return null;
    }
}
