package com.onemount.crm.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    @Size(min = 2,max = 50, message = "Full name length must be between 2 and 50")
    private String fullname;

    @Email
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Mobie number must be betwwen 10 and 11")
    private String mobile;

    // public CustomerDTO(String fullname, String email, String mobile) {
    //     this.fullname = fullname;
    //     this.email = email;
    //     this.mobile = mobile;
    // }

}
