package com.tabcorp.qa.adyen;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Locale;

// Created by Adyen
public class ClientSideEncrypter {

    private static final String PREFIX="adyenjs_";
    private static final String VERSION="0_1_18";
    private static final String SEPARATOR="$";

    private PublicKey pubKey;
    private Cipher aesCipher;
    private Cipher rsaCipher;
    private SecureRandom srandom;

    private static final Logger log = LoggerFactory.getLogger(ClientSideEncrypter.class);


    public ClientSideEncrypter (String publicKeyString) throws Exception {
        srandom = new SecureRandom();
        String[] keyComponents = publicKeyString.split("\\|");

        // The bytes can be converted back to a public key object
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.toString());
            return;
        }

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(
                new BigInteger(keyComponents[1].toLowerCase(Locale.getDefault()), 16),
                new BigInteger(keyComponents[0].toLowerCase(Locale.getDefault()), 16));

        try {
            pubKey = keyFactory.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new Exception("Problem reading public key: " + publicKeyString, e);
        }

        try {
            Security.addProvider(new BouncyCastleProvider());
            aesCipher  = Cipher.getInstance("AES/CCM/NoPadding", "BC");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Problem instantiation AES Cipher Algorithm", e);
        } catch (NoSuchPaddingException e) {
            throw new Exception("Problem instantiation AES Cipher Padding", e);
        } catch (NoSuchProviderException e) {
            log.error(e.toString());
        }

        try {
            rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Problem instantiation RSA Cipher Algorithm", e);
        } catch (NoSuchPaddingException e) {
            throw new Exception("Problem instantiation RSA Cipher Padding", e);
        } catch (InvalidKeyException e) {
            throw new Exception("Invalid public key: " + publicKeyString, e);
        }

    }

    public String encrypt(String plainText) throws Exception {
        SecretKey aesKey = generateAESKey(128);

        byte[] iv = generateIV(12);

        byte[] encrypted;
        try {
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(iv));
            // getBytes is UTF-8 on Android by default
            encrypted = aesCipher.doFinal(plainText.getBytes());
        } catch (IllegalBlockSizeException e) {
            throw new Exception("Incorrect AES Block Size", e);
        } catch (BadPaddingException e) {
            throw new Exception("Incorrect AES Padding", e);
        } catch (InvalidKeyException e) {
            throw new Exception("Invalid AES Key", e);
        } catch(InvalidAlgorithmParameterException e) {
            throw new Exception("Invalid AES Parameters", e);
        }

        byte[] result = new byte[iv.length + encrypted.length];
        // copy IV to result
        System.arraycopy(iv, 0, result, 0, iv.length);
        // copy encrypted to result
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

        byte[] encryptedAESKey;
        try {
            encryptedAESKey = rsaCipher.doFinal(aesKey.getEncoded());
            return String.format("%s%s%s%s%s%s", PREFIX, VERSION, SEPARATOR, Base64.getEncoder().encodeToString(encryptedAESKey), SEPARATOR, Base64.getEncoder().encodeToString(result));
        } catch (IllegalBlockSizeException e) {
            throw new Exception("Incorrect RSA Block Size", e);
        } catch (BadPaddingException e) {
            throw new Exception("Incorrect RSA Padding", e);
        }
    }

    private SecretKey generateAESKey(int keySize) throws Exception {
        KeyGenerator kgen;
        try {
            kgen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Unable to get AES algorithm", e);
        }
        kgen.init(keySize);
        return kgen.generateKey();
    }

    /**
     * Generate a random Initialization Vector (IV)
     *
     * @param ivSize
     * @return the IV bytes
     */
    private synchronized byte[] generateIV(int ivSize) {
        byte[] iv = new byte[ivSize];//generate random IV AES is always 16bytes, but in CCM mode this represents the NONCE
        srandom.nextBytes(iv);
        return iv;
    }

}