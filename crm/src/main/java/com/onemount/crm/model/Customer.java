package com.onemount.crm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "customer")
@Table(name="customer")
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Size(min = 2,max = 50, message = "Full name length must be between 2 and 50")
    private String fullname;

    @Email
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Mobie number must be betwwen 10 and 11")
    private String mobile;

}