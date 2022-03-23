package com.mab.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignatureUtil {

    public static String getSignature(String apiId, String apiKey, String transactionType, String httpMethod,
            String requestTimeStamp, String nonce, String requestBody) {
        String requestBodyBase64 = null;
        String signatureRaw = null;
        String signatureBase64 = null;

        log.info("apiId : " + apiId);
        log.info("apiKey : " + apiKey);
        log.info("transactionType : " + transactionType);
        log.info("httpMethod : " + httpMethod);
        log.info("requestTimeStamp : " + requestTimeStamp);
        log.info("nonce : " + nonce);
        log.info("requestBody : " + requestBody);

        requestBodyBase64 = Base64.encodeBase64String(requestBody.getBytes());
        log.info("requestBodyBase64 : " + requestBodyBase64);

        signatureRaw = apiId + transactionType + requestTimeStamp + httpMethod + nonce + requestBodyBase64;
        log.info("signatureRaw : " + signatureRaw);

        signatureBase64 = getBase64HmacSha256(apiKey, signatureRaw);
        log.info("SignatureBase64 : " + signatureBase64);

        return signatureBase64;
    }

    public static String getBase64HmacSha256(String apiKey, String rawData) {
        String result = null;
        Mac sha256_HMAC;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(apiKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            result = Base64.encodeBase64String(sha256_HMAC.doFinal(rawData.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return result;
    }

}