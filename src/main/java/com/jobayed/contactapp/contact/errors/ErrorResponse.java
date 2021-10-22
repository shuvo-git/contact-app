package com.jobayed.contactapp.contact.errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse
{
    private List<ErrorModel> errorMessages;
    private String message;
    private int status;
}
