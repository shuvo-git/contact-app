package com.jobayed.contactapp.contact.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity(name = "contacts")
public class Contact implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false,length = 20)
    private Long id;

    @Column(length = 50, nullable = false)
    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotBlank(message = "Contact Number must not be empty")
    @Column(name = "contact_number",length = 11, nullable = false)
    private String contactNumber;

    @NotBlank(message = "Address must not be empty")
    @Column(length = 50, nullable = false)
    private String address;

    @Min(1)
    @Max(5000)
    @Column(name = "post_code",length = 4, nullable = false)
    private int postCode;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
}
