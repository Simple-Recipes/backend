package com.recipes.exception;

/**
 * Exception thrown when a favorite is not found.
 */
public class FavoriteNotFoundException extends BaseException {

    public FavoriteNotFoundException() {
        super("Favorite not found");
    }

    public FavoriteNotFoundException(String msg) {
        super(msg);
    }

}
