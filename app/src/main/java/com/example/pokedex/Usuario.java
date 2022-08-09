package com.example.pokedex;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Usuario {
    private static final Usuario INSTANCE = new Usuario();
    private String userID;

    private Usuario(){
        userID = "";
    }
    public static Usuario getInstance(){
        return INSTANCE;
    }

    public void setUserID(String userID) throws Exception {
        if(!this.userID.equals(""))
            throw new Exception("El ID de usuario ya ha sido establecido");
        this.userID = getSHA256(userID);
    }

    private String getSHA256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(input.getBytes("utf8"));
        return String.format("%064x", new BigInteger(1, digest.digest()));
    }

    public String getUserID(){
        return this.userID;
    }

}