package src.ACL;

import src.Errors.PasswordError;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Users implements Serializable {
    static final long serialVersionUID = 42L;

    public String name_user;
    public String hash_pass;
    public Groups group;

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
    }

}
