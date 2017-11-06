package liub.com.mylibrary.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA算法
 * Created by liub on 2017/10/26 .
 */
public class RSAUtil {

    private final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAIinxzq7wEx9fEKjQ1xez0U/58l7ebLntaVHOdkNhhwFjOTZBozmomKLf8G5oTL + BjM + DiHVjxbxKs1uES2SSKNTx59qw5UGsowbRPcI6XzcHMsbLD3CvFdStQ + ilj4zALK + Nwgs1510bRvQE9PYX2 + pamAmgMhuNZ2C8oMabaQIDAQAB";
//    private final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC69veKW1X9GETEFr49gu9PN8w7H6alWec8wmF8SoP3tqQLAflZp8g83UZPX2UWhClnm53P5ZwesaeSTHkXkSI0iSjwd27N07bc8puNgB5BAGhJ80KYqTv3Zovl04C8AepVmxy9iFniJutJSYYtsRcnHYyUNoJai4VXhJsp5ZRMqwIDAQAB";
    private String privateKey = "MIICXgIBAAKBgQDAIinxzq7wEx9fEKjQ1xez0U/58l7ebLntaVHOdkNhhwFjOTZBozmomKLf8G5oTL + BjM + DiHVjxbxKs1uES2SSKNTx59qw5UGsowbRPcI6XzcHMsbLD3CvFdStQ + ilj4zALK + Nwgs1510bRvQE9PYX2 + pamAmgMhuNZ2C8oMabaQIDAQABAoGBAJK7xLLieSHqSMQuke7kxjpTObQW0TMncBuLmqmGElGybHHd6LzaLpe + 8mz6TyeirYMACh9XIOfwxKt8LQFtHsFYzDLdbCQgatWNtfgspdQ3D5tUG82yHb5hRKkwnSNXEGXnFafLmgRCMs4BPBwX9uV / NpE4bnPLXSOAlomAwbWBAkEA9FHIcWJlSXU3Bgt5lTOO + tNfnHNEdOH6m5OKbT / cplVLe + kQAI0mg6ZTnArjOunUL70icfafKgREbfWmhCZVcQJBAMlRrZw42pg38Q5C52f / nWYOv0uyH + zFLMWmt + w8pfc80kdCeBUMaQzw / YFwfifWNs2fMzaFGnvUH59nLZ97SXkCQQDLyQtvx6rEQsjKeffHw2GXRYeSb3LCK1tlOQNVbNcWxj5X9GYsj494b9t9ZHd0RGWADDoW5KaN3VFWhneF8pOBAkATzqV2KWHYeCiPbqW15cBmuiy4Fd5uPTgaejy + UXlCygkmWbPW3lG3pD5M7fR7luftcyxmwNHKPSSfWldyyX6BAkEAw9w6rB8OWV6qwWJ4OFZ / cRLJO + aZqNVGDMx9s1Y8LTXCG6bHIqYOlijnhpLhkFuD6zf6S7lddg + hvNF5AjAC1A ==";
//    private String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDbODPX+TrymksM5+aRF3K5b/cDT+fDq5GKfgGlZO8MlgEG4UpOEh097UYIYrWEzBQs53MDn3dIwlMt4X+4FuKcECG3+drZ3Ehzh68qDak+q/2pIiEb0QfdCULY8Xz3TIoN0EKnCCVoHkHo3+Vel3uaUtlU74bnB0CCw1jGWdJOSeEpUiU5MjujjyzYbOVsz4AdzbmEfZFB8e+mSsM7zjcriyUponkMSXBKIiDQehKn0YLp39N2sJrpG/2SOcZnPh3wc/k6OtJP52CHVMDjrB4wTdf6njiqntGub+nuiIJDDGl+4HzahVfQ5IruMbWThRQzUC+KF0Q2tIFIMc0XuXfJAgMBAAECggEBALUXFmyg67shDkJZBzRwREs/XLQVvAT9o7reIIn6eSbSe4KtdO5NNG7FpQakVAKhe0Ek6PPjNWybao8KKrFt5kC5asFJ2yoBOLCHM4HvyxGEjoS7NtJ9uJs1XU1NH8hCKAEFOyo0JoJ+DEBNRHMBfA+dxP7O54fNi9L5gEpKRNp50ePm20H5m2rHRu6nhOloAJZA+jXog3Yc2283Bnjx1PwpkP8LZypC+TodIOx3aZMRoXYSz6BRia8ylar1tr2Y8oJoOV8ENWGp6z0tj/YH/Cwc9jYHlHJWcMdzkTmlohwAiJmsFjRAsn8ognhmHoDFplaWM7Zlof5AhNSDXRwdHYkCgYEA7/dL9f3M0ArdfutBmx67O1l48HsAUG/O8dX49oVoCZI+91C/NyQ0r1A6iu9qjSvmIPBmbIOeGH0kRbl4BwRGF1j1wVB2rgl5FFf5oYs6TPvgGR9losvd4Bk3UOsua9nUkpmHOE/6ONZYGo2PhpYRhTc5/pOMW7jEW0UgicJd2e8CgYEA6d4HvU4nx8jWhFo4IDmb9lE6ccoG+6gUGSxkzQZUtfa+H/EsFlQv3GNGvXNQ/oQ3oAQQrFxfvOSQ51MXED2MOKluGqy7DSSkp2ocRfFU7wEbordU3rhfKMROsj1NYMj4anUWT4AnsVc8JezatKViWbXmwuyk1plovjaNWEFm4ccCgYA6nDUybP4EZlL5N+67O4NRmKXgXrqR6u0pxjBbzfO+OrkkYNWDW1V+6GKUIqvstSctLmpl4LPRmWctnIJDfHi+JR1JTSTflzK6lE5FdaMUwIRYvoFthMu5e482NWsOLpMsB1GuoGImVbwJKEBBCBBZcEa69kDW+kcDX9v1qcKQgwKBgQDkzNE7lpv6rHWa1P02IcaBDGUmcCW2zXCkVDdmEnyL7ZOCgpvEWKbecc4CiTBDYS1egruhNVqA3gkaF8Nnox9tS+2pcTYrHJ9uHrT8hKe4kJft8Hi60RxgMPZhEPKD7vqChHzIWLP8n0D8RaaOt4LqC7lxGL4IKdw8w/gy0QGLfwKBgQDN4POsfAgXieHkFZdOoc9++memQFgYQHVgfrepAw2MZxV+YX/75FkymHxKrONCAqFs7bS1u2GfPRpkpiYbyHmboAkBgK3AiUYExpBUs27ZjwP3FSNLYxT9Pe4f/tOXpmSpZnaiXvUJ16OPCBLRHf77+C8DQ8NLnVCkSGdW4/nd/w==";

//    private final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKeDkRea/pgX2K6V/eOCQ8kImiqH+QkONx+ubVkvME9mr0I4lElNr8Hl1Q2zEQ+zz4zgDyOWoiCBxSwagBPTate15aMs+uplJL74ScW5gduBYcoQfHURC/ORCFGoa5Y6049p1lhow31yKkKGNTYQYupdlR4HFbqESOPFMwFnG0gQIDAQAB";
//    private String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMp4ORF5r+mBfYrpX944JDyQiaKof5CQ43H65tWS8wT2avQjiUSU2vweXVDbMRD7PPjOAPI5aiIIHFLBqAE9Nq17Xloyz66mUkvvhJxbmB24FhyhB8dREL85EIUahrljrTj2nWWGjDfXIqQoY1NhBi6l2VHgcVuoRI48UzAWcbSBAgMBAAECgYAtLeaOH7lBQcPh23GpBJ4RZa9QvIi6mZonNPWNct0HnnT/RW67/vtehugLwt2QDH/uhQlxA57LOUQYs13p6N7qMZ+4YY592hw4hrJUEAuuORU+wKWnr+wVQNm6Qc9Qf7axM6B5NgtLPbf0R7M53vgHHMyJh2tJKrY3RUdBbsUugQJBAObj3+B7v2QVKKPZlYvICwbKZAUcb1qZtPjtw7+aDah0EEqkaYD0ytmjl2esoknPySN2gbouc+nDvYZopFLgiDMCQQDgfRqCYfMHhjHPHoOwco3ZAevDDe22QksBIkfgFB9srEJCWauFyvB5PTG6+wFv94zqy3R92C6AVaWn8Ae8uqx7AkBkroWXfB7PY7KfEGh31bmJMoQ+/lFIbrJNwlCTonfGNyZLhjpDc3tpQD7rhIoYKbWJ80lKiKsfCq4AiGzvft2lAkEAqcBQDGmu0XC7N2hWolVtR7x5H8znhNuKRfg7K4lr3cxAalXOKuSzhKoucbqecqFZsK5aj1Kqjya0llIeN6tdAwJAImLxsxLxhk6dc8slEo8ObLAWWWkRZNiXCpr+2aWspVx1cK3GRtAa+0Q7X0TiA62/CrlWR/xJHvDI/+I9mcxJKg==";
//    private final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDaYk2kFeSHIUVYSrQ+XDYfHoeXDyuKGvl+B3VYuoc7mwM3fmtJStnxC9SbqCa9ZbBD+qLv5jm6Cy/Hy+ojodLzkSh1sFWwXllsKvJ6WoZf/DKZZwIbmM5XxjSRmB8TqaZothjaDvyXguITiUpMF14SyUozt2hfOe6WzPkio+ef+QIDAQAB";
//    private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANpiTaQV5IchRVhKtD5cNh8eh5cPK4oa+X4HdVi6hzubAzd+a0lK2fEL1JuoJr1lsEP6ou/mOboLL8fL6iOh0vORKHWwVbBeWWwq8npahl/8MplnAhuYzlfGNJGYHxOppmi2GNoO/JeC4hOJSkwXXhLJSjO3aF857pbM+SKj55/5AgMBAAECgYEAnOQg2XBtBPPPKPZKdb6zlznMuabJZeepNHTRRlSDkTv1XKnzE+21k6onLUjpxAnJT51B783bzR/6TLTf2oeIz+XAaz6fXMp19/2eUGMhsvtd+Ib+tc5opIT1vJ7yoRWqpWocTVYqTWSUWprPoOo65GW0+QAYjAyM3C3RKCoaOnECQQDwiu3tviqSXQvwlWU/NlsvaztJxsn8PjBbdhcO2WupPuGAktzv0YGGxNjYIxYwQoPRqTuCvQzwMtRIwIq7zp7dAkEA6GrZoiOrekV4o0oGZu1l6Hcdcx3VesCKQbVDzphZvgR/99FtdRQAfJuNBtefSZfDOfodOMxsIAZuMrL+8hr9zQJARQgZ8Di7DvYVDx5QyCtZT+p+QVYDcZiHWDgztdc/xnp9TODY9xoxCfREJwMqwv7Txb/DjGmk5Ooxcg+PwQYzIQJAQK6T+PLSY0n4RAFy/KwvA147HPF9mxcAoz7OKy3tM9vOvWpUexPg+szTl8bkdre8xmLcRRVnNhZ0DroQB9MJNQJBAJe5MELSfYYME7lBodvsXjfz+F724Yk8WmOPyUsAGRVm1Ud5V+hKA9t7w2CToOJD28DH9t7VmVbHLVV6yHlNI5o=";

//    public static final String ssoPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMp4ORF5r+mBfYrpX944JDyQiaKof5CQ43H65tWS8wT2avQjiUSU2vweXVDbMRD7PPjOAPI5aiIIHFLBqAE9Nq17Xloyz66mUkvvhJxbmB24FhyhB8dREL85EIUahrljrTj2nWWGjDfXIqQoY1NhBi6l2VHgcVuoRI48UzAWcbSBAgMBAAECgYAtLeaOH7lBQcPh23GpBJ4RZa9QvIi6mZonNPWNct0HnnT/RW67/vtehugLwt2QDH/uhQlxA57LOUQYs13p6N7qMZ+4YY592hw4hrJUEAuuORU+wKWnr+wVQNm6Qc9Qf7axM6B5NgtLPbf0R7M53vgHHMyJh2tJKrY3RUdBbsUugQJBAObj3+B7v2QVKKPZlYvICwbKZAUcb1qZtPjtw7+aDah0EEqkaYD0ytmjl2esoknPySN2gbouc+nDvYZopFLgiDMCQQDgfRqCYfMHhjHPHoOwco3ZAevDDe22QksBIkfgFB9srEJCWauFyvB5PTG6+wFv94zqy3R92C6AVaWn8Ae8uqx7AkBkroWXfB7PY7KfEGh31bmJMoQ+/lFIbrJNwlCTonfGNyZLhjpDc3tpQD7rhIoYKbWJ80lKiKsfCq4AiGzvft2lAkEAqcBQDGmu0XC7N2hWolVtR7x5H8znhNuKRfg7K4lr3cxAalXOKuSzhKoucbqecqFZsK5aj1Kqjya0llIeN6tdAwJAImLxsxLxhk6dc8slEo8ObLAWWWkRZNiXCpr+2aWspVx1cK3GRtAa+0Q7X0TiA62/CrlWR/xJHvDI/+I9mcxJKg==";

    static{
        System.loadLibrary("native-str");
    }

    /**
     * 获取sso私钥
     * @return
     */
    public static final native String getSSOPrivateKey();

    /**
     * 加密算法RSA
     */
    public final static String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public final static String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private final static String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private final static String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private final static int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private final static int MAX_DECRYPT_BLOCK = 128;

    private static RSAUtil rsaUtil;

    public static RSAUtil getInstance() {
        if (rsaUtil == null)
            rsaUtil = new RSAUtil();
        return rsaUtil;
    }

    private RSAUtil() {
    }

    /**
     * 生成密钥对(公钥和私钥)
     */
    public Map<String, Key> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Key> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 用私钥对信息生成数字签名
     */
    public String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeToString(signature.sign(), Base64.NO_WRAP);
    }

    /**
     * 校验数字签名
     */
    public boolean verify(String data, String sign) throws Exception {
        return verify(data,publicKey,sign);
    }

    /**
     * 校验数字签名
     */
    public boolean verify(String data, String publicKey, String sign) throws Exception {
//        byte[] dataBytes =data.getBytes("UTF-8");
//        byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
//        PublicKey publicK = keyFactory.generatePublic(keySpec);
//        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//        signature.initVerify(publicK);
//        signature.update(dataBytes);
//
//        return signature.verify(Base64.decode(sign, Base64.DEFAULT));

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey,Base64.DEFAULT));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

        signature.initVerify(pubKey);
        signature.update(data.getBytes("UTF-8"));

        return signature.verify(Base64.decode(sign, Base64.DEFAULT));
    }

    /**
     * 私钥解密
     */
    public String decryptByPrivateKey(String content) throws Exception {
        return decryptByPrivateKey(content, privateKey);
    }

    /**
     * 私钥解密
     */
    public String decryptByPrivateKey(String content, String privateKey) throws Exception {
        byte[] encryptedData = Base64.decode(content, Base64.DEFAULT);
        byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//        Cipher cipher = Cipher.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
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
        return new String(decryptedData);
    }

    /**
     * 公钥解密
     */
    public String decryptByPublicKey(String content) throws Exception {
        return decryptByPublicKey(content, publicKey);
    }

    /**
     * 公钥解密
     */
    public String decryptByPublicKey(String content, String publicKey) throws Exception {
        byte[] encryptedData = Base64.decode(content, Base64.DEFAULT);
        byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
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
        return new String(decryptedData);
    }

    /**
     * 公钥加密
     */
    public String encryptByPublicKey(String content) throws Exception {
        return encryptByPublicKey(content, publicKey);
    }

    /**
     * 公钥加密
     */
    public String encryptByPublicKey(String content, String publicKey) throws Exception {
        try {
            byte[] data = content.getBytes();
            byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
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
            return Base64.encodeToString(encryptedData, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (IOException e) {
            throw new Exception("公钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 私钥加密
     */
    public String encryptByPrivateKey(String content) throws Exception {
        return encryptByPrivateKey(content, privateKey);
    }

    /**
     * 私钥加密
     */
    public String encryptByPrivateKey(String content, String privateKey) throws Exception {
        byte[] data = content.getBytes();
        byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
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
        return Base64.encodeToString(encryptedData, Base64.NO_WRAP);
    }

    /**
     * 获取私钥
     */
    public String getPrivateKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = keyMap.get(PRIVATE_KEY);
        return Base64.encodeToString(key.getEncoded(), Base64.NO_WRAP);
    }

    /**
     * 获取公钥
     */
    public String getPublicKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = keyMap.get(PUBLIC_KEY);
        return Base64.encodeToString(key.getEncoded(), Base64.NO_WRAP);
    }
}