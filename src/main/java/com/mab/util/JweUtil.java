package com.mab.util;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.lang.JoseException;
import org.jose4j.zip.CompressionAlgorithmIdentifiers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JweUtil {

    public static String jweEncrypt(String jweKey, String payload) throws JoseException {
        log.info("Body Request : " + payload);

        JsonWebEncryption jsonWebEncryption = new JsonWebEncryption();
        jsonWebEncryption.setPayload(payload);
        jsonWebEncryption.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A256KW);
        jsonWebEncryption
                .setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
        jsonWebEncryption.setKey(setJsonWebKeyKey(jweKey).getKey());
        jsonWebEncryption.setCompressionAlgorithmHeaderParameter(CompressionAlgorithmIdentifiers.DEFLATE);
        String jwe = jsonWebEncryption.getCompactSerialization();

        log.info("JsonWebKey : " + setJsonWebKeyKey(jweKey).toJson());
        log.info("JWE Header : " + jsonWebEncryption.toString());
        log.info("JWE : " + jwe);

        return jwe;
    }

    private static JsonWebKey setJsonWebKeyKey(String keyString) throws JoseException {
        return JsonWebKey.Factory.newJwk("{\"kty\":\"oct\",\"k\":\"" + keyString + "\"}");
    }

    public static String jweDecrypt(String jweKey, String jweString) throws JoseException {
        log.info("Encrypt String : " + jweString);

        JsonWebEncryption jsonWebEncryption = new JsonWebEncryption();
        jsonWebEncryption.setAlgorithmConstraints(
                new AlgorithmConstraints(ConstraintType.PERMIT, KeyManagementAlgorithmIdentifiers.A256KW));
        jsonWebEncryption.setContentEncryptionAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.PERMIT,
                ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512));
        jsonWebEncryption.setCompactSerialization(jweString);
        jsonWebEncryption.setKey(setJsonWebKeyKey(jweKey).getKey());
        String payload = jsonWebEncryption.getPayload();

        log.info("JsonWebKey : " + setJsonWebKeyKey(jweKey).toJson());
        log.info("JWE Header : " + jsonWebEncryption.toString());
        log.info("Payload : " + payload);

        return payload;
    }

}
