package com.jobayed.contactapp.contact.resources;

import com.jobayed.contactapp.contact.domain.Contact;
import com.jobayed.contactapp.contact.errors.ErrorModel;
import com.jobayed.contactapp.contact.errors.ErrorResponse;
import com.jobayed.contactapp.contact.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200")
@RequestMapping("api/v1/contact")
@RequiredArgsConstructor
@RestController
@Validated
public class ContactResource
{
    private final ContactService contactService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Contact contact){
        Contact contact1 = contactService.create(contact);
        return ResponseEntity.ok().body(contact1);
    }

    @GetMapping
    public ResponseEntity<List<Contact>> show(){
        List<Contact> contacts = contactService.getAll();
        return ResponseEntity.ok().body(contacts);
    }

    /**
     * Method that check against {@code @Valid} Objects passed to controller endpoints
     *
     * @param exception
     * @return a {@code ErrorResponse}
     *
     */
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MethodArgumentNotValidException exception) {

        List<ErrorModel> errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(err -> new ErrorModel(err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
                .distinct()
                .collect(Collectors.toList());
        return ErrorResponse.builder()
                .errorMessages(errorMessages)
                .message("Validation Error")
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    /**
     * Handle unprocessable json data exception
     * @param msgNotReadable
     * @return a {@code ErrorResponse}
     */
    @ExceptionHandler(value= HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleUnprosseasableMsgException(HttpMessageNotReadableException msgNotReadable) {
        // note that we've added new properties (message, status) to our ErrorResponse model return ErrorResponse.builder()
        return ErrorResponse.builder()
                .message("UNPROCESSABLE INPUT DATA")
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .build();
    }
}
