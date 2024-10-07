package com.Uber.Project.UberApp.DTO;

import com.Uber.Project.UberApp.Entity.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
}
