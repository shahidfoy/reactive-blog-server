package com.shahidfoy.reactiveblog.exception_handlers;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String id) {
        super("Post: " + id + " is not found");
    }
}
