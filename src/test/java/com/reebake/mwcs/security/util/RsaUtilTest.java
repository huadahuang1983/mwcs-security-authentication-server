package com.reebake.mwcs.security.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class RsaUtilTest {

    @TempDir
    File tempDir;

    @Test
    void testGenerateKeyPair() throws NoSuchAlgorithmException {
        KeyPair keyPair = RsaUtil.generateKeyPair();
        assertNotNull(keyPair);
        assertNotNull(keyPair.getPublic());
        assertNotNull(keyPair.getPrivate());
    }

    @Test
    void testGenerateKeyPairWithCustomSize() throws NoSuchAlgorithmException {
        KeyPair keyPair = RsaUtil.generateKeyPair(4096);
        assertNotNull(keyPair);
        assertNotNull(keyPair.getPublic());
        assertNotNull(keyPair.getPrivate());
    }

    @Test
    void testStoreAndLoadKeyPair() throws Exception {
        String keyPath = tempDir.getAbsolutePath();
        String keyName = "test-key";

        // 生成并存储密钥对
        KeyPair generatedKeyPair = RsaUtil.generateAndStoreKeyPair(keyPath, keyName);
        assertNotNull(generatedKeyPair);

        // 验证文件是否创建
        File publicKeyFile = new File(keyPath, keyName + ".public.key");
        File privateKeyFile = new File(keyPath, keyName + ".private.key");
        assertTrue(publicKeyFile.exists());
        assertTrue(privateKeyFile.exists());

        // 加载密钥对
        KeyPair loadedKeyPair = RsaUtil.loadKeyPair(keyPath, keyName);
        assertNotNull(loadedKeyPair);

        // 验证密钥是否一致
        assertEquals(generatedKeyPair.getPublic().getAlgorithm(), loadedKeyPair.getPublic().getAlgorithm());
        assertEquals(generatedKeyPair.getPrivate().getAlgorithm(), loadedKeyPair.getPrivate().getAlgorithm());
    }

    @Test
    void testStoreAndLoadPublicKey() throws Exception {
        String keyPath = tempDir.getAbsolutePath();
        String keyName = "test-public-key";

        // 生成并存储密钥对
        KeyPair keyPair = RsaUtil.generateAndStoreKeyPair(keyPath, keyName);
        String publicKeyPath = keyPath + File.separator + keyName + ".public.key";

        // 加载公钥
        PublicKey loadedPublicKey = RsaUtil.loadPublicKey(publicKeyPath);
        assertNotNull(loadedPublicKey);
        assertEquals(keyPair.getPublic().getAlgorithm(), loadedPublicKey.getAlgorithm());
    }

    @Test
    void testStoreAndLoadPrivateKey() throws Exception {
        String keyPath = tempDir.getAbsolutePath();
        String keyName = "test-private-key";

        // 生成并存储密钥对
        KeyPair keyPair = RsaUtil.generateAndStoreKeyPair(keyPath, keyName);
        String privateKeyPath = keyPath + File.separator + keyName + ".private.key";

        // 加载私钥
        PrivateKey loadedPrivateKey = RsaUtil.loadPrivateKey(privateKeyPath);
        assertNotNull(loadedPrivateKey);
        assertEquals(keyPair.getPrivate().getAlgorithm(), loadedPrivateKey.getAlgorithm());
    }

    @Test
    void testEncryptDecrypt() throws Exception {
        String testContent = "Hello, RSA Encryption!";

        // 生成密钥对
        KeyPair keyPair = RsaUtil.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 加密
        String encryptedContent = RsaUtil.encrypt(testContent, publicKey);
        assertNotNull(encryptedContent);
        assertNotEquals(testContent, encryptedContent);

        // 解密
        String decryptedContent = RsaUtil.decrypt(encryptedContent, privateKey);
        assertNotNull(decryptedContent);
        assertEquals(testContent, decryptedContent);
    }
}