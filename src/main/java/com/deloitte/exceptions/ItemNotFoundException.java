package com.deloitte.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
    
    public ItemNotFoundException(Long id) {
        super("Item not found with id: " + id);
    }
}
