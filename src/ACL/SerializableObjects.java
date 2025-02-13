package src;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class SerializableObjects {
    public String name_file;
    public static String name_file_default = new Date(System.currentTimeMillis()).toString();
    public ArrayList<Users>  users;
    public ArrayList<Groups> groups;

    SerializableObjects(String name_file) {
        if (name_file == null) {
            name_file = name_file_default;
        }
        this.name_file = name_file;
    }

    void load_users_and_groups() throws IOException, ClassNotFoundException {
        if (users != null) {
            users = new ArrayList<>();
        }
        if (groups != null) {
            groups = new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.name_file))) {
            while (true) { // Intentamos leer hasta que ocurra EOFException
                try {
                    Object obj = in.readObject();
                    if (obj instanceof Users) {
                        users.add((Users) obj);
                    } else if (obj instanceof Groups) {
                        groups.add((Groups) obj);
                    }
                } catch (EOFException e) {
                    break; // Fin del archivo alcanzado, salir del bucle
                }
            }
        }
    }

    void write_users_and_groups() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(
                        this.name_file
                )
        );

        // serializar los groups
        if (groups != null) {
            for (Groups group : groups) {
                out.writeObject(group);
            }
        }

        // serializar los usuarios
        if (users != null) {
            for (Users user : users) {
                out.writeObject(user);
            }
        }
        out.close();
    }
}
