package com.mab.format.sign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class RequestFormat {

    private String apiId;
    private String apiKey;
    private String transactionType;
    private String httpMethod;
    private String requestBodyEncrypt;

}
