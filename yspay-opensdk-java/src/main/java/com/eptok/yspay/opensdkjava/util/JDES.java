package com.eptok.yspay.opensdkjava.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class JDES {

    private byte[] m_desKey;
    private static JDES instance;

    public JDES() {
    }

    public static synchronized JDES getInstanse() {
        if (instance == null) {
            instance = new JDES();
        }
        return instance;
    }

    public void SetKey(byte[] desKey) {
        this.m_desKey = desKey;
    }


    public byte[] doECBEncrypt(byte[] plainText, int len) throws Exception {
        Cipher cipher = this.getCipher("DES", "DES/ECB/PKCS5Padding", 1, (byte[])null);
        byte[] encryptedData = cipher.doFinal(plainText, 0, len);
        return encryptedData;
    }


    public byte[] doECBDecrypt(byte[] encryptText, int len) throws Exception {
        Cipher cipher = this.getCipher("DES", "DES/ECB/PKCS5Padding", 2, (byte[])null);
        byte[] decryptedData = cipher.doFinal(encryptText, 0, len);
        return decryptedData;
    }


    protected Cipher getCipher(String factory, String cipherName, int cryptMode, byte[] iv) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte[] rawKeyData = this.m_desKey;
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(factory);
        SecretKey key = new SecretKeySpec(this.m_desKey, factory);
        Cipher cipher = Cipher.getInstance(cipherName);
        if (iv != null) {
            IvParameterSpec ips = new IvParameterSpec(iv);
            cipher.init(cryptMode, key, ips, sr);
        } else {
            cipher.init(cryptMode, key, sr);
        }

        return cipher;
    }
}