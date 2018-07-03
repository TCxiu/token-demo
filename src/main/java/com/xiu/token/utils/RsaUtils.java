package com.xiu.token.utils;

import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * author: Wangzhan
 * data :2018/4/7 0007 20:29
 * version:2.0.0
 * description:RSA加密
 */
public class RsaUtils {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static final String KEY_ALGORITHM = "RSA";
    private static KeyFactory keyFactory = null;
    public static String PrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ93N1LtWHpGirSmsX6Ng9wP6OSWfvtDYRf9LI/BkBZaaJvkkIz11VnS1IHKos+sOGNyRJ7FEMSIaVoS+3mRg15pT4vDJWQYnnt+0nmquvsj35bbchXXQuWnpyXWdplsyE06z6gBpGZ/DEvEDknQEAiJ7/AtqxdW79iW+oP05CV3AgMBAAECgYAFi21Ee0Ds2x6xWybKl99A8CBQvJc744L2vx0wobIkrwKdO/C87ULw7MkBWB+l1OexwGobe9+97Oys13artiu5H+ACjuwVYB/aqF29sVh8wgb1vVJIvPKkTs5i+bUeKZFcHXrkqY/p9A0vhstlYEpXzpIIp43WE+Fx3iYez1mxkQJBAM/vl221C3roXe4UTWr/ZaHaiJIYIWKGWJYKtwBc8O60wWyjs/EDIqVQbvYKhVX2S0gW6IhKrebeMjAcjtrEE10CQQDEU3Iiphg1r6zUXqIrTo70YE4LqXeI4pDeaBXEoB4CJM0ImXt//UxPkQPhzlrEMnGjr6bp3xMRORnAror8nULjAkEApBM5WO7v1Rb/7zoD8LcAehuXgjgaN8Usp5Bi4J1129WzfscZ2MaRkwKJ3AlnNvJ2arbw24KaAD86OO5/5q2rXQJAfvJaRg9SbjyHK9d5Uzl79IblUGMnWQrvk/CHytmiLc6wCZR9KBiYpIZnlTZCuMTJeIdXDbjh8mwSu+XhF1F08wJAaflq07zSWGbFw0wv6APU8cT3kUp279H3kKnb0/ow7JGeZx7O3T+hxFo/nslNUUn2R2QP0GzqvsfqHUakwr+cJw==";
    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfdzdS7Vh6Roq0prF+jYPcD+jkln77Q2EX/SyPwZAWWmib5JCM9dVZ0tSByqLPrDhjckSexRDEiGlaEvt5kYNeaU+LwyVkGJ57ftJ5qrr7I9+W23IV10Llp6cl1naZbMhNOs+oAaRmfwxLxA5J0BAIie/wLasXVu/YlvqD9OQldwIDAQAB";

    static {
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密方法
     *
     * @param dataStr 要解密的数据
     * @return 解密后的原数据
     * @throws Exception
     */
    public static String decrypt(String dataStr) throws Exception {
        //要加密的数据
        //对私钥解密
        Key decodePrivateKey = getPrivateKeyFromBase64KeyEncodeStr(PrivateKey);
        //Log.i("机密",""+decodePrivateKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, decodePrivateKey);
        byte[] encodedData = Base64.decode(dataStr);
        byte[] decodedData = cipher.doFinal(encodedData);
        String decodedDataStr = new String(decodedData, "utf-8");
        return decodedDataStr;
    }

    public static Key getPrivateKeyFromBase64KeyEncodeStr(String keyStr) {
        byte[] keyBytes = new byte[0];
        keyBytes = Base64.decode(keyStr);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        Key privateKey = null;
        try {
            privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 获取base64加密后的字符串的原始公钥
     *
     * @param keyStr
     * @return
     */
    public static Key getPublicKeyFromBase64KeyEncodeStr(String keyStr) {
        byte[] keyBytes = new byte[0];
        keyBytes = Base64.decode(keyStr);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        Key publicKey = null;
        try {
            publicKey = keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 公钥加密方法
     * @param dataStr 要加密的数据
     * @param dataStr 公钥base64字符串
     * @return 加密后的base64字符串
     * @throws Exception
     */
    public static String encryptPublicKey(String dataStr) throws Exception {

        //要加密的数据
        byte[] data = dataStr.getBytes();
        // 对公钥解密
        Key decodePublicKey = getPublicKeyFromBase64KeyEncodeStr(publicKey);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, decodePublicKey);
        byte[] encodedData = cipher.doFinal(data);
        String encodedDataStr = new String(Base64.encode(encodedData));
        return encodedDataStr;
    }

    /**
     * 使用公钥进行分段加密
     * @param dataStr 要加密的数据
     * @return 公钥base64字符串
     * @throws Exception
     */
    public static String encryptByPublicKey(String dataStr)
            throws Exception {
        //要加密的数据
        byte[] data = dataStr.getBytes();
        // 对公钥解密
        Key decodePublicKey = getPublicKeyFromBase64KeyEncodeStr(publicKey);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, decodePublicKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        String encodedDataStr = new String(Base64.encode(encryptedData));
        return encodedDataStr;
    }

    /**
     * 使用私钥进行分段解密
     *
     * @param dataStr 使用base64处理过的密文
     * @return 解密后的数据
     * @throws Exception
     */
    public static String decryptByPrivateKey(String dataStr)
            throws Exception {

        byte[] encryptedData = Base64.decode(dataStr);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key decodePrivateKey = getPrivateKeyFromBase64KeyEncodeStr(PrivateKey);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        // Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, decodePrivateKey);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        String decodedDataStr = new String(decryptedData, "utf-8");
        return decodedDataStr;
    }
}
