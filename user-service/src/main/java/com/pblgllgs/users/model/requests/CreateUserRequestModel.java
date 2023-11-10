package com.pblgllgs.users.model.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequestModel {

    @NotBlank(message = "First name can not be null")
    @Size(message = "First name would be have 2 min and 20 max characters ",min = 2,max = 20)
    private String firstName;

    @NotBlank(message = "Last name can not be null")
    @Size(message = "Last name would be have 2 min and 20 max characters ",min = 2,max = 20)
    private String lastName;

    @NotBlank(message = "Password can not be null")
    @Size(message = "Last name would be have 2 min and 20 max characters ",min = 8,max = 16)
    private String password;

    @NotBlank(message = "Email name can not be null")
    @Email(message = "Is not a valid email")
    private String email;
}
