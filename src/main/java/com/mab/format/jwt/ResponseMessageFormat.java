package com.mab.format.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "JWT", "AuthString" })
@JsonInclude(Include.NON_NULL)
public class ResponseMessageFormat {

    private String jwt;
    private String authString;

}
