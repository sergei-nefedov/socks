package pers.nefedov.socks.exceptions;

public class NoSuchSocksInStockException extends RuntimeException {
    public NoSuchSocksInStockException(String message) {
        super(message);
    }
}