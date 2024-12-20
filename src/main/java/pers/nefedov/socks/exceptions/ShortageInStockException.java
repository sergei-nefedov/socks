package pers.nefedov.socks.exceptions;

public class ShortageInStockException extends RuntimeException {
    public ShortageInStockException(String message) {
        super(message);
    }
}