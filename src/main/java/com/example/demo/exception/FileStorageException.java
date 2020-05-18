package com.example.demo.exception;

/**
 * Exception file storage
 * 
 * @author hao.cu
 * @since 2020/5/16
 *
 */
public class FileStorageException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
