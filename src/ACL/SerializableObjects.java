package src.ACL;

import src.Commands.Command;
import src.Focus.Focus;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class SerializableObjects {
    public String name_file;
    public static String name_file_default = new SimpleDateFormat(
            "yyyy").format(new Date()) + ".dat";
    public static ArrayList<Groups> groups;

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
        Users user = root.users.get(0);
        user.init_commands_permission();
        for (Map.Entry<Class<? extends Command>, Boolean> entry : user.commands_permission.entrySet()) {
            // poner para el usuario root, todos los comandos como permitidos
            user.commands_permission.put(entry.getKey(), true);
        }

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
        ObjectOutputStream out = null;

        if (exists) {
            // Si el archivo ya existe, lee los objetos primero
            ArrayList<Groups> existingGroups = new ArrayList<>();
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                while (true) {
                    try {
                        Object obj = in.readObject();
                        if (obj instanceof Groups) {
                            existingGroups.add((Groups) obj);
                        }
                    } catch (EOFException e) {
                        break; // Fin del archivo alcanzado, salir del bucle
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Crear un nuevo ObjectOutputStream que sobrescriba el archivo
            out = new ObjectOutputStream(new FileOutputStream(file));

            // Añadir solo los grupos que no están ya en la lista
            if (groups != null) {
                for (Groups group : groups) {
                    if (!existingGroups.contains(group)) { // Evitar duplicados
                        out.writeObject(group);
                    }
                }
            }

        } else {
            // Si el archivo no existe, escribe directamente
            out = new ObjectOutputStream(new FileOutputStream(file));
            if (groups != null) {
                for (Groups group : groups) {
                    out.writeObject(group);
                }
            }
        }

        out.close();
    }
}
