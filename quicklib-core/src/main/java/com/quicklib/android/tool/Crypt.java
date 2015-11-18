package com.quicklib.android.tool;

import android.util.Base64;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class provides simple method to encrypt and decrypt text
 *
 * @author Benoit Deschanel
 * @since 15-11-17
 * Copyright (C) 2015 Quicklib
 */
public class Crypt {


    /** The Constant ALGO. */
    private static final String ALGO = "AES";

    /** The pass. */
    private byte[] pass = null;

    /** The keyspec. */
    private SecretKeySpec keyspec;

    /** The cipher. */
    private Cipher cipher = null;

    /** The base64 mode */
    private int base64Mode = Base64.URL_SAFE;



    /**
     * Instantiates a new crypt.
     *
     * @param pass the pass
     */
    public Crypt(String pass) {
        init(pass.getBytes());
    }


    /**
     * Instantiates a new crypt with the base64 mode
     * @see <a href="http://developer.android.com/reference/android/util/Base64.html">Base64</a>
     *
     * @param pass the pass
     * @param base64Mode defines the base64 mode
     */
    public Crypt(String pass, int base64Mode) {
        init(pass.getBytes());
        if(Arrays.asList(new int[]{Base64.CRLF, Base64.DEFAULT, Base64.NO_CLOSE, Base64.NO_PADDING, Base64.NO_WRAP, Base64.URL_SAFE}).contains(base64Mode) ){
            this.base64Mode = base64Mode;
        }else{
            throw new IllegalArgumentException("Base64 mode should be one of the following values: " + Base64.CRLF+", "+ Base64.DEFAULT+", "+ Base64.NO_CLOSE+", "+ Base64.NO_PADDING+", "+ Base64.NO_WRAP+", "+ Base64.URL_SAFE + " ( @see http://developer.android.com/reference/android/util/Base64.html )" );
        }
    }

    private void init(byte[] pass){
        if (this.pass == null) {
            try {
                this.pass = pass;
                this.keyspec = new SecretKeySpec(pass, ALGO);
                this.cipher = Cipher.getInstance(ALGO);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("*** ERROR *** " + e.getMessage());
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                System.out.println("*** ERROR *** " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Encrypt.
     *
     * @param text the text
     * @return the encrypted string
     */
    public String encrypt(String text) {
        return new String(encrypt(text, true));
    }

    /**
     * Encrypt.
     *
     * @param text the text
     * @param b64 the b64
     * @return the byte[]
     */
    public byte[] encrypt(String text, boolean b64) {
        try {
            byte[] bytes = null;

            cipher.init(Cipher.ENCRYPT_MODE, this.keyspec);
            bytes = cipher.doFinal(text.getBytes());
            if (b64) {
                return Base64.encode(bytes, base64Mode);
            } else {
                return bytes;
            }

        } catch (InvalidKeyException e) {
            System.out.println("*** ERROR *** " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            System.out.println("*** ERROR *** " + e.getMessage());
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            System.out.println("*** ERROR *** " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypt.
     *
     * @param crypted the crypted
     * @return the decrypted string
     */
    public String decrypt(String crypted) {
        return new String(decrypt(crypted, true));
    }

    /**
     * Decrypt.
     *
     * @param crypted the crypted
     * @param b64 the b64
     * @return the byte[]
     */
    public byte[] decrypt(String crypted, boolean b64) {
        try {
            byte[] bytes = null;
            if (b64) {
                bytes = Base64.decode(crypted, base64Mode);
            } else {
                bytes = crypted.getBytes();
            }

            cipher.init(Cipher.DECRYPT_MODE, this.keyspec);
            return cipher.doFinal(bytes);

        } catch (InvalidKeyException e) {
            System.out.println("*** ERROR *** " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.out.println("*** ERROR *** " + e.getMessage());
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("*** ERROR *** " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Md5.
     *
     * @param s the s
     * @return the string
     */
    public static String md5(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }



    /**
     * Sha1.
     *
     * @param s the s
     * @return the string
     */
    public static String sha1(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }


    /**
     * Generate key.
     *
     * @param length the length
     * @return the String
     */
    public static String generateKey(int length) {
        ByteBuffer bab = null;
        switch (length) {
            case 128:
            case 192:
            case 256:
                bab =  ByteBuffer.allocate(length / 8);
                break;
            default:
                throw new IllegalArgumentException("The key length must be one of the following values: 128, 192, 256");
        }

        Random generator = new Random();
        while (bab.remaining() > 0) {
            int value = generator.nextInt(128);
            if ((value > 47 && value < 58) || (value > 64 && value < 91) || (value > 96 && value < 123)) {
                bab.put((byte)value);
            }
        }
        return new String(bab.array());
    }


    /**
     * Bytes to hex.
     *
     * @param b the b
     * @return the string
     */
    public static String bytesToHex(byte[] b) {
        char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < b.length; j++) {
            buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
            buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }

}