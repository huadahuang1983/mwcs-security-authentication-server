package com.reebake.mwcs.security.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * RSA工具类，用于生成、存储和加载RSA密钥对
 */
public class RsaUtil {

    private static final Logger logger = LoggerFactory.getLogger(RsaUtil.class);

    private static final String RSA_ALGORITHM = "RSA";
    private static final int DEFAULT_KEY_SIZE = 2048;
    private static final String PUBLIC_KEY_SUFFIX = ".public.key";
    private static final String PRIVATE_KEY_SUFFIX = ".private.key";

    /**
     * 生成RSA密钥对
     *
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果RSA算法不可用
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(DEFAULT_KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 生成RSA密钥对
     *
     * @param keySize 密钥大小（1024, 2048, 4096等）
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果RSA算法不可用
     */
    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 将密钥对存储到文件
     *
     * @param keyPair 密钥对
     * @param basePath 密钥文件存储的基础路径
     * @param keyName 密钥名称（用于生成文件名）
     */
    public static void storeKeyPair(KeyPair keyPair, String basePath, String keyName) {
        try {
            // 创建目录（如果不存在）
            FileUtil.mkdir(basePath);

            // 存储公钥
            String publicKeyPath = basePath + File.separator + keyName + PUBLIC_KEY_SUFFIX;
            PublicKey publicKey = keyPair.getPublic();
            byte[] publicKeyBytes = publicKey.getEncoded();
            String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);
            FileUtil.writeUtf8String(publicKeyBase64, publicKeyPath);
            logger.info("公钥已存储到: {}", publicKeyPath);

            // 存储私钥
            String privateKeyPath = basePath + File.separator + keyName + PRIVATE_KEY_SUFFIX;
            PrivateKey privateKey = keyPair.getPrivate();
            byte[] privateKeyBytes = privateKey.getEncoded();
            String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKeyBytes);
            FileUtil.writeUtf8String(privateKeyBase64, privateKeyPath);
            logger.info("私钥已存储到: {}", privateKeyPath);
        } catch (Exception e) {
            logger.error("存储密钥对失败: {}", e.getMessage(), e);
            throw new RuntimeException("存储密钥对失败", e);
        }
    }

    /**
     * 从文件加载密钥对
     *
     * @param basePath 密钥文件存储的基础路径
     * @param keyName 密钥名称
     * @return 加载的密钥对
     */
    public static KeyPair loadKeyPair(String basePath, String keyName) {
        try {
            // 加载公钥
            String publicKeyPath = basePath + File.separator + keyName + PUBLIC_KEY_SUFFIX;
            String publicKeyBase64 = FileUtil.readUtf8String(publicKeyPath);
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
            PublicKey publicKey = java.security.KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(new java.security.spec.X509EncodedKeySpec(publicKeyBytes));

            // 加载私钥
            String privateKeyPath = basePath + File.separator + keyName + PRIVATE_KEY_SUFFIX;
            String privateKeyBase64 = FileUtil.readUtf8String(privateKeyPath);
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
            PrivateKey privateKey = java.security.KeyFactory.getInstance(RSA_ALGORITHM).generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(privateKeyBytes));

            logger.info("从文件加载密钥对成功");
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            logger.error("从文件加载密钥对失败: {}", e.getMessage(), e);
            throw new RuntimeException("从文件加载密钥对失败", e);
        }
    }

    /**
     * 从文件加载公钥
     *
     * @param publicKeyPath 公钥文件路径
     * @return 加载的公钥
     */
    public static PublicKey loadPublicKey(String publicKeyPath) {
        try {
            String publicKeyBase64 = FileUtil.readUtf8String(publicKeyPath);
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
            return java.security.KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(new java.security.spec.X509EncodedKeySpec(publicKeyBytes));
        } catch (Exception e) {
            logger.error("加载公钥失败: {}", e.getMessage(), e);
            throw new RuntimeException("加载公钥失败", e);
        }
    }

    /**
     * 从文件加载私钥
     *
     * @param privateKeyPath 私钥文件路径
     * @return 加载的私钥
     */
    public static PrivateKey loadPrivateKey(String privateKeyPath) {
        try {
            String privateKeyBase64 = FileUtil.readUtf8String(privateKeyPath);
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
            return java.security.KeyFactory.getInstance(RSA_ALGORITHM).generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(privateKeyBytes));
        } catch (Exception e) {
            logger.error("加载私钥失败: {}", e.getMessage(), e);
            throw new RuntimeException("加载私钥失败", e);
        }
    }

    /**
     * 使用Hutool的RSA工具进行加密
     *
     * @param content   要加密的内容
     * @param publicKey 公钥
     * @return 加密后的Base64字符串
     */
    public static String encrypt(String content, PublicKey publicKey) {
        RSA rsa = new RSA(null, publicKey);
        return rsa.encryptBase64(content, KeyType.PublicKey);
    }

    /**
     * 使用Hutool的RSA工具进行解密
     *
     * @param encryptedContent 加密后的Base64字符串
     * @param privateKey       私钥
     * @return 解密后的内容
     */
    public static String decrypt(String encryptedContent, PrivateKey privateKey) {
        RSA rsa = new RSA(privateKey, null);
        return rsa.decryptStr(encryptedContent, KeyType.PrivateKey);
    }

    /**
     * 生成RSA密钥对并存储到指定路径
     *
     * @param keyPath 密钥存储路径
     * @param keyName 密钥名称
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果RSA算法不可用
     */
    public static KeyPair generateAndStoreKeyPair(String keyPath, String keyName) throws NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair();
        storeKeyPair(keyPair, keyPath, keyName);
        return keyPair;
    }

    /**
     * 生成RSA密钥对并存储到指定路径
     *
     * @param keyPath  密钥存储路径
     * @param keyName  密钥名称
     * @param keySize  密钥大小
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果RSA算法不可用
     */
    public static KeyPair generateAndStoreKeyPair(String keyPath, String keyName, int keySize) throws NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair(keySize);
        storeKeyPair(keyPair, keyPath, keyName);
        return keyPair;
    }
}