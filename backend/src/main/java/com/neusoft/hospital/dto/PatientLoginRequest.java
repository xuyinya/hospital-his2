package com.neusoft.hospital.dto;

import lombok.Data;

@Data
public class PatientLoginRequest {
    private String idCard;
    private String password;
}
