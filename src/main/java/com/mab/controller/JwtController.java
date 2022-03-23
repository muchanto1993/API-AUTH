package com.mab.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mab.format.jwt.RequestFormat;
import com.mab.format.jwt.ResponseFormat;
import com.mab.format.jwt.ResponseMessageFormat;
import com.mab.util.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class JwtController {

        // Creating Object of ObjectMapper define in Jakson Api
        private ObjectMapper objectMapper = new ObjectMapper();

        @PostMapping("/jwt")
        public ResponseEntity<ResponseFormat> apiJwt(HttpServletRequest request,
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

                // Create JWT
                String jwt = JwtUtil.getJwt(requestBody.getSignatureBase64(), nonce, requestTimeStamp,
                                requestBody.getJwtKey());

                /* Informasi Tentang Nama Method */
                String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

                ResponseMessageFormat responseMessageFormat = new ResponseMessageFormat();
                responseMessageFormat.setJwt(jwt);
                responseMessageFormat.setAuthString("Bearer " + requestBody.getApiId() + jwt);

                ResponseFormat responseFormat = new ResponseFormat();
                responseFormat.setTimestamp(new Date());
                responseFormat.setStatus(HttpStatus.OK.value());
                responseFormat.setError("");
                responseFormat.setMessage(responseMessageFormat);
                responseFormat.setPath(request.getRequestURI() + " | " + nameofCurrMethod);

                ResponseEntity<ResponseFormat> responseEntity = null;
                responseEntity = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                                .body(responseFormat);

                log.info("Respose Message : " + objectMapper.writeValueAsString(responseEntity));

                return responseEntity;
        }

}
