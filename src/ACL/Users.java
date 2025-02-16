package src.ACL;

import src.Commands.Command;
import src.Commands.CreateFocus;
import src.Commands.DeleteFocus;
import src.Commands.ParamFormatter;
import src.Errors.PasswordError;

import java.io.FilenameFilter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Users implements Serializable {
    static final long serialVersionUID = 42L;
    // Lista de clases a excluir
    public static final ArrayList<Class<?>> CLASS_EXCLUIR = new ArrayList<>(Arrays.asList(
            Command.class,
            ParamFormatter.class
    ));

    public String name_user;
    public String hash_pass;
    public Groups group;

    // permisos de cada comando para el usuario
    public HashMap<Class<? extends Command>, Boolean> commands_permission;
    private String CalcHash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
        byte[] digest = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length ; i++) {
            hexString.append(Integer.toHexString(0xFF & digest[i]));
        }
        return hexString.toString();
    }

    // Método de ejemplo para obtener clases de un paquete
    public void init_commands_permission() {

        this.commands_permission = new HashMap<>();
        // Obtener clases del paquete "src.Commands", excluyendo las especificadas
        try {
            Set<Class<?>> commandClasses = getClassesInPackage("src.Commands", CLASS_EXCLUIR);
            for (Class<?> clazz : commandClasses) {
                // Verificar que la clase es una subclase de Command antes de agregarla al HashMap
                if (Command.class.isAssignableFrom(clazz) && !clazz.equals(Command.class)) {
                    //System.out.println("Clase encontrada: " + clazz.getName());
                    this.commands_permission.put((Class<? extends Command>) clazz, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Método para obtener todas las clases en un paquete, excluyendo las especificadas
    public static Set<Class<?>> getClassesInPackage(String packageName, ArrayList<Class<?>> class_excluir) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        // Convertir el nombre del paquete en una ruta
        String path = packageName.replace('.', '/');
        URL packageURL = Users.class.getClassLoader().getResource(path);
        if (packageURL != null) {
            File directory = new File(packageURL.getFile());
            if (directory.exists() && directory.isDirectory()) {
                // Filtrar archivos .class dentro del directorio
                String[] classFiles = directory.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".class");
                    }
                });

                // Cargar cada clase
                for (String classFile : classFiles) {
                    String className = packageName + '.' + classFile.substring(0, classFile.length() - 6); // Eliminar ".class"
                    Class<?> clazz = Class.forName(className);
                    // Excluir las clases especificadas
                    if (!class_excluir.contains(clazz)) {
                        classes.add(clazz);
                    }
                }
            }
        }
        return classes;
    }
    private String get_build_text_for_hash(String name_user, String password) {
        return "%d:%d:%s:%s".formatted(
                name_user.length(),
                password.length(),
                name_user, password
        );
    }

    /*
     * calcula el hash de una posible password
     */
    Users (
            String name_user,
            String password,
            Groups group
    ) throws NoSuchAlgorithmException {
        this.name_user = name_user;
        this.hash_pass = CalcHash(get_build_text_for_hash(name_user, password));
        this.group = group;
        init_commands_permission();
    }

    /*
     * Compara un hash de password con una posible password
     */
    public Users(
            String name_user,
            String password,
            String hash_pass,
            Groups group
    ) throws PasswordError, NoSuchAlgorithmException {
        String hash = CalcHash(get_build_text_for_hash(name_user, password));

        this.name_user = name_user;
        this.hash_pass = hash_pass;
        this.group = group;

        if (!hash.equals(hash_pass)) {
            throw new PasswordError("Error pass for user %s in group %s SHA256(%s)".formatted(
                    name_user, group, hash
            ));
        }
        init_commands_permission();
    }

}
