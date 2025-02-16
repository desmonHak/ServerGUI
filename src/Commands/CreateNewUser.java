package src.Commands;

import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;

import src.ACL.Groups;
import src.ACL.SerializableObjects;
import src.ACL.Users;
import src.Errors.PasswordError;
import src.Pair;

public class CreateNewUser extends Command{
    public CreateNewUser(String command, JFrame windows) {
        super(command, windows);
    }

    @Override
    public String exec(PrintWriter out, Pair<Groups, Users> dataUser) {
        if (this.id.equalsIgnoreCase("createNewUser")
                && does_he_have_permissions(dataUser, this.getClass())) {
            // createNewUser<"root", "user", "password">

            // clase de formateo de datos
            ParamFormatter param = new ParamFormatter(this.params);

            // pasa a string el primer argumento
            String
                    s_group_name = param.asString(0),
                    s_user = param.asString(1),
                    s_pass = param.asString(2);

            boolean estatus = false;
            for (Groups group: SerializableObjects.groups) { // buscar si el grupo existe
                if(group.name.equals(s_group_name)){
                    for (Users user: group.users) {
                        if (user.name_user.equals(s_user)) {
                            // usuario ya existente
                            return String.valueOf(false);
                        }
                    }
                    // si el usuario no esta repetido
                    try {
                        group.users.add(new Users(s_user, s_pass, group));
                    } catch (NoSuchAlgorithmException e) {
                        this.ret_client(out, e.getMessage());
                        return null;
                    }
                    estatus = true;
                    break;
                }
            }

            this.ret_client(out, String.valueOf(estatus));

        }
        this.ret_client(out); // retornar
        return null;
    }
}
