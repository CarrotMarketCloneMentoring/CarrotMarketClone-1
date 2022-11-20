package com.daangn.clone.encryption;

import com.daangn.clone.common.response.ApiException;
import com.daangn.clone.common.response.ApiResponseStatus;
import org.apache.commons.codec.DecoderException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Hex;

@Component
public class AES128 {
    private String ips;
    private Key keySpec;
    private String uuidKey;
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public AES128() {
        try {
            //어차피 싱글톤으로 관리되니깐 , 스프링이 실행되면서 이 AES128 객체 딱 한번 생성될 떄 - uuid 값도 딱 한번 생성되어 해당 객체의 필드로 보관됨
            this.uuidKey = UUID.randomUUID().toString();

            byte[] keyBytes = new byte[16];
            byte[] b = uuidKey.getBytes("UTF-8");
            System.arraycopy(b, 0, keyBytes, 0, keyBytes.length);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            this.encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.ips = uuidKey.substring(0, 16);
            this.keySpec = keySpec;

            encryptCipher.init(Cipher.ENCRYPT_MODE, this.keySpec,
                    new IvParameterSpec(this.ips.getBytes("UTF-8")));

            decryptCipher.init(Cipher.DECRYPT_MODE, this.keySpec,
                    new IvParameterSpec(this.ips.getBytes("UTF-8")));

        } catch (Exception e) {
            throw new ApiException(ApiResponseStatus.FAIL_CREATE_AES128, "AES128 인스턴스 생성중 예외가 터졌습니다.");
        }
    }



    public String encrypt(String str) {

        try {
            byte[] encrypted = encryptCipher.doFinal(str.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(encrypted));
        } catch (Exception e) {
            throw new ApiException(ApiResponseStatus.FAIL_ENCRYPT, "AES128 암호화 하는 과정에를 예외가 터졌습니다.");
        }
    }

    public String decrypt(String str) {
        try {
            byte[] byteStr = Base64.decodeBase64(str.getBytes());
            return new String(decryptCipher.doFinal(byteStr), "UTF-8");
        } catch (Exception e) {
            throw new ApiException(ApiResponseStatus.FAIL_DECRYPT,"AES128 복호화 하는 과정에서 예외가 터졌습니다.");
        }
    }


}
