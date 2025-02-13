package src.ACL;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SerializableObjects {
    public String name_file;
    public static String name_file_default = new SimpleDateFormat(
            "yyyy").format(new Date()) + ".dat";
    public ArrayList<Groups> groups;

    public SerializableObjects(String name_file) {
        if (name_file == null) {
            name_file = name_file_default;
        }
        this.name_file = name_file;
    }

    public void load_default_users_and_groups() throws NoSuchAlgorithmException {
        groups = new ArrayList<>();

        // añadir grupo root
        groups.add(
                new Groups("root")
        );
        Groups root = groups.get(0);
        root.users = new ArrayList<>();

        // añadir usuario root:
        root.users.add(
                new Users(
                        "root",
                        "1234",
                        root
                )
        );
    }

    public void load_users_and_groups() throws IOException, ClassNotFoundException {
        if (groups == null) {
            groups = new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.name_file))) {
            while (true) { // Intentamos leer hasta que ocurra EOFException
                try {
                    Object obj = in.readObject();
                    /*if (obj instanceof Users) {
                        users.add((Users) obj);
                    } else*/ if (obj instanceof Groups) {
                        groups.add((Groups) obj);
                    }
                } catch (EOFException e) {
                    break; // Fin del archivo alcanzado, salir del bucle
                }
            }
        }
    }

    public void write_users_and_groups() throws IOException {
        File file = new File(this.name_file);
        boolean exists = file.exists();
        ObjectOutputStream out;

        if (exists) {
            // Si el archivo ya existe, usa un ObjectOutputStream sin cabecera para evitar errores
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file, true))) {
                protected void writeStreamHeader() throws IOException {
                    reset(); // Evita escribir una nueva cabecera que corrompa el archivo
                }
            };
        } else {
            // Si el archivo no existe, usa un ObjectOutputStream normal
            out = new ObjectOutputStream(new FileOutputStream(file));
        }


        // serializar los groups
        if (groups != null) {
            for (Groups group : groups) {
                out.writeObject(group);
            }
        }

        // serializar los usuarios
        /*if (users != null) {
            for (Users user : users) {
                out.writeObject(user);
            }
        }*/
        out.close();
    }
}
