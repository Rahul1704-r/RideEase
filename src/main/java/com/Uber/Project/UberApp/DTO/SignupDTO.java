package com.Uber.Project.UberApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SignupDTO {

    private String name;
    private String email;
    private String password;

}
