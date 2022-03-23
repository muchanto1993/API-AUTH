package com.mab.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mab.format.jwe.RequestFormat;
import com.mab.format.jwe.ResponseFormat;
import com.mab.util.JweUtil;

import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class JweController {

    // Creating Object of ObjectMapper define in Jakson Api
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${jweKey}")
    private String jweKey;

    @PostMapping("/encryptRequestBody")
    public ResponseEntity<ResponseFormat> apiEncryptRequestBody(HttpServletRequest request,
            @RequestBody Object requestBody) throws JoseException, JsonProcessingException {
        log.info("Request Client : " + request.getRemoteAddr());

        try {
            log.info("Request Message : " + objectMapper.writeValueAsString(requestBody));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Informasi Tentang Nama Method */
        String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setTimestamp(new Date());
        responseFormat.setStatus(HttpStatus.OK.value());
        responseFormat.setError("");
        responseFormat.setMessage(JweUtil.jweEncrypt(jweKey, objectMapper.writeValueAsString(requestBody)));
        responseFormat.setPath(request.getRequestURI() + " | " + nameofCurrMethod);

        ResponseEntity<ResponseFormat> responseEntity = null;
        responseEntity = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(responseFormat);
        try {
            log.info("Respose Message : " + objectMapper.writeValueAsString(responseEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseEntity;
    }

    @PostMapping("/decryptRequestBody")
    public ResponseEntity<ResponseFormat> apiDecryptRequestBody(HttpServletRequest request,
            @RequestBody RequestFormat requestBody) throws JoseException {
        log.info("Request Client : " + request.getRemoteAddr());

        try {
            log.info("Request Message : " + objectMapper.writeValueAsString(requestBody));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Informasi Tentang Nama Method */
        String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setTimestamp(new Date());
        responseFormat.setStatus(HttpStatus.OK.value());
        responseFormat.setError("");
        responseFormat.setMessage(JweUtil.jweDecrypt(jweKey, requestBody.getEncryptString()));
        responseFormat.setPath(request.getRequestURI() + " | " + nameofCurrMethod);

        ResponseEntity<ResponseFormat> responseEntity = null;
        responseEntity = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(responseFormat);
        try {
            log.info("Respose Message : " + objectMapper.writeValueAsString(responseEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseEntity;
    }

}
