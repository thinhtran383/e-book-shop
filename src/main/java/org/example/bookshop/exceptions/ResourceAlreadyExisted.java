package org.example.bookshop.exceptions;

public class ResourceAlreadyExisted extends RuntimeException {
    public ResourceAlreadyExisted(String message) {
        super(message);
    }
}
