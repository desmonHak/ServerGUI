package src.ACL;

import src.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

public class Groups implements Serializable {
    static final long serialVersionUID = 42L;

    public final String name;
    public ArrayList<Users> users;

    Groups(String name) {
        this.name = name;
    }

    public Users getUserByName(String username) {
        for (Users user : users) {
            if(username.equals(user.name_user)) {
                return user;
            }
        }
        return null;
    }

    public static Pair<Groups, Users> searchUserNameInGroup(ArrayList<Groups> groups, String username) {
        for (Groups group : groups) {
            Users user = group.getUserByName(username);
            if (user != null) {
                return new Pair<>(group, user); // retornar el grupo y usuario
            }
        }
        return null;
    }

}
