package com.Uber.Project.UberApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderDTO {

    private Long id;
    private UserDTO user;
    private Double rating;

}
