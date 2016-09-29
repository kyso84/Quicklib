package com.quicklib.android.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.quicklib.android.core.tool.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * @author bdescha1
 * @since 16-07-20
 * Copyright (C) 2016 French Connection !!!
 */
public final class AndroCrypt {
    private static final String TAG = "AndroCrypt";
    private static final int ITERATION = 1000;
    private static final String SECRET_ALGO = "PBKDF2WithHmacSHA1";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";


    private final Cipher cipher;
    private final SecretKey secretKey;
    private final IvParameterSpec ivParameterSpec;
    private boolean debug = false;

    public static final class Builder {
        private SharedPreferences pref;
        private byte[] sharedKey;
        private byte[] iv;
        private String password;
        private Algo algo = Algo.AES256;
        private boolean debug = false;


        public Builder(Context context) {
            this.pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
            this.sharedKey = pref.getString("k0", "").getBytes();
            this.iv = pref.getString("k1", "").getBytes();
            this.password = context.getPackageName();
        }

        public Builder(Context context, String password) {
            this.pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
            this.sharedKey = pref.getString("k0", "").getBytes();
            this.iv = pref.getString("k1", "").getBytes();
            this.password = password;
        }

        public Builder(String password) {
            this.password = password;
        }

        public Builder(byte[] sharedKey, byte[] iv) {
            this.sharedKey = sharedKey;
            this.iv = iv;
        }

        public Builder(String sharedKey, String iv) {
            this.sharedKey = sharedKey.getBytes();
            this.iv = iv.getBytes();
        }

        public Builder setAlgo(Algo algo) {
            this.algo = algo;
            return this;
        }

        public Builder setDebug(boolean enable) {
            this.debug = enable;
            return this;
        }


        public AndroCrypt create() throws Exception {
            SecretKey secretKey;
            IvParameterSpec ivParameterSpec;
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

            // Create an instance based on existing configuration
            if (sharedKey != null && sharedKey.length > 0 && iv != null && iv.length > 0) {
                if (sharedKey.length != (algo.length / 8)) {
                    throw new Exception("SharedKey length must be " + (algo.length / 8));
                }

                if (iv.length != cipher.getBlockSize()) {
                    throw new Exception("IV length must be " + cipher.getBlockSize());
                }

                secretKey = new SecretKeySpec(sharedKey, algo.getAlgo());
                ivParameterSpec = new IvParameterSpec(iv);
            } else if (password != null && password.length() > 0) {
                // Create a new instance
                SecureRandom random = new SecureRandom();
                secretKey = generateSecret(random, password, algo);
                ivParameterSpec = generateIV(random, cipher);
            } else {
                throw new IllegalArgumentException("Invalid parameters");
            }


            AndroCrypt instance = new AndroCrypt(cipher, secretKey, ivParameterSpec, this.debug);
            if (pref != null) { // In "Managed" mode we store the generated configuration
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("k0", instance.getSecretKey());
                editor.putString("k1", instance.getIV());
                editor.apply();
            }
            return instance;
        }
    }

    private AndroCrypt(Cipher cipher, SecretKey secretKey, IvParameterSpec ivParameterSpec, boolean debug) {
        this.cipher = cipher;
        this.secretKey = secretKey;
        this.ivParameterSpec = ivParameterSpec;
        this.debug = debug;
        if (debug) {
            log("SecretKey: " + getSecretKey());
            log("IV: " + getIV());
        }
    }

    public String encrypt(String text) {
        byte[] result = new byte[0];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            result = cipher.doFinal(text.getBytes());

            result = Base64.encode(result, Base64.URL_SAFE);
        } catch (BadPaddingException e) {
            log(e);
        } catch (InvalidKeyException e) {
            log(e);
        } catch (IllegalBlockSizeException e) {
            log(e);
        } catch (InvalidAlgorithmParameterException e) {
            log(e);
        }
        log("encrypt: " + text + " => " + new String(result));
        return new String(result);
    }


    public String decrypt(String text) {
        byte[] result = new byte[0];
        try {
            result = Base64.decode(text.getBytes(), Base64.URL_SAFE);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            result = cipher.doFinal(result);
        } catch (BadPaddingException e) {
            log(e);
        } catch (InvalidKeyException e) {
            log(e);
        } catch (IllegalBlockSizeException e) {
            log(e);
        } catch (InvalidAlgorithmParameterException e) {
            log(e);
        }
        log("decrypt: " + text + " => " + new String(result));
        return new String(result);
    }

    public String getIV() {
        return new String(ivParameterSpec.getIV());
    }

    public String getSecretKey() {
        return new String(secretKey.getEncoded());
    }

    private void log(Throwable e) {
        if (debug) {
            e.printStackTrace();
        }
    }

    private void log(String text) {
        if (debug) {
            Log.d( text);
        }
    }


    public enum Algo {
        AES512(512, "AES"),
        AES256(256, "AES"),
        AES128(128, "AES");

        private final int length;
        private final String algo;

        Algo(int length, String algo) {
            this.length = length;
            this.algo = algo;
        }

        public String getAlgo() {
            return algo;
        }

        public int getLength() {
            return length;
        }
    }


    public static SecretKey generateSecret(SecureRandom random, String password, Algo algo) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[algo.getLength() / 8];
        random.nextBytes(salt);

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION, algo.getLength());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_ALGO);
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();

        return new SecretKeySpec(keyBytes, algo.getAlgo());
    }


    private static IvParameterSpec generateIV(SecureRandom random, Cipher cipher) {
        byte[] iv = new byte[cipher.getBlockSize()];
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }


    public static String getHmac256(String secret, String message) {
        return getHmac(Algo.AES256, secret, message);
    }

    public static String getHmac512(String secret, String message) {
        return getHmac(Algo.AES512, secret, message);
    }

    private static String getHmac(Algo algo, String secret, String message) {
        String hash = "";
        Mac mac;
        try {
            String algoName = "HmacSHA" + algo.length;
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), algoName);

            mac = Mac.getInstance(algoName);
            mac.init(secretKey);

            byte[] raw = mac.doFinal(message.getBytes());


            StringBuilder sb = new StringBuilder(raw.length * 2);
            for (byte b : raw) {
                sb.append(String.format("%02x", b & 0xff));
            }
            hash = sb.toString();

        } catch (Exception e) {
        }
        return hash;
    }


}