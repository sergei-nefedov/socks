package pers.nefedov.socks.exceptions;

public class FileUploadErrorException extends RuntimeException{
    public FileUploadErrorException(String message) {
        super(message);
    }
}
