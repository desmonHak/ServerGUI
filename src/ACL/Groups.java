package src;

import java.io.Serializable;
import java.util.ArrayList;

public class Groups implements Serializable {

    public final String name;
    public ArrayList<Users> users;

    Groups(String name) {
        this.name = name;
    }

}
