package com.mab.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import com.mab.Model.AuthClaims;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {

    public static String getJwt(String signatureBase64, String nonce, String requestTimeStamp, String jwtKey) {
        AuthClaims authClaims = new AuthClaims();
        authClaims.setSignatureBase64(signatureBase64);
        authClaims.setNonce(nonce);
        authClaims.setRequestTimeStamp(requestTimeStamp);

        String authClaimsString = JsonUtil.objectToJsonString(authClaims);

        Key key = null;
        try {
            key = new HmacKey(jwtKey.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setHeader("typ", "JWT");
        jws.setKey(key);
        jws.setPayload(authClaimsString);

        String jwt = null;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException e) {
            e.printStackTrace();
        }

        log.info("JWT Header : " + jws.toString());
        log.info("JWT Claims : " + authClaimsString);
        log.info("JWT : " + jwt);

        return jwt;
    }

}
