package com.mab.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mab.format.sign.RequestFormat;
import com.mab.format.sign.ResponseFormat;
import com.mab.util.SignatureUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SignController {

    // Creating Object of ObjectMapper define in Jakson Api
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/sign")
    public ResponseEntity<ResponseFormat> apiSign(HttpServletRequest request,
            @RequestBody RequestFormat requestBody) throws JsonProcessingException {
        log.info("Request Client : " + request.getRemoteAddr());
        log.info("Request Message : " + objectMapper.writeValueAsString(requestBody));

        // Create Unix Timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000);
        String requestTimeStamp = "" + timestamp.getTime();

        // Create Nonce
        UUID uuid = UUID.randomUUID();
        String nonce = uuid.toString();
        nonce = nonce.replace("-", "");

        // Create Signature
        String signatureBase64 = SignatureUtil.getSignature(requestBody.getApiId(), requestBody.getApiKey(),
                requestBody.getTransactionType(), requestBody.getHttpMethod(),
                requestTimeStamp, nonce, requestBody.getRequestBodyEncrypt());

        /* Informasi Tentang Nama Method */
        String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setTimestamp(new Date());
        responseFormat.setStatus(HttpStatus.OK.value());
        responseFormat.setError("");
        responseFormat.setMessage(signatureBase64);
        responseFormat.setPath(request.getRequestURI() + " | " + nameofCurrMethod);

        ResponseEntity<ResponseFormat> responseEntity = null;
        responseEntity = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(responseFormat);

        log.info("Respose Message : " + objectMapper.writeValueAsString(responseEntity));

        return responseEntity;
    }

}
