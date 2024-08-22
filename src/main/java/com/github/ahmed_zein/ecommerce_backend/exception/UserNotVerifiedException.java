package com.github.ahmed_zein.ecommerce_backend.exception;

public class UserNotVerifiedException extends Exception {
    private final boolean newEmailSent;

    public UserNotVerifiedException(boolean newEmailSent) {
        this.newEmailSent = newEmailSent;
    }

    public boolean isNewEmailSent() {
        return newEmailSent;
    }

}
