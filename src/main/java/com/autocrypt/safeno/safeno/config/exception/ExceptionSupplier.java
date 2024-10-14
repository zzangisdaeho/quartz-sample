package com.autocrypt.safeno.safeno.config.exception;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class ExceptionSupplier {

    public static Supplier<CustomException> propertyNotFound(Long carId) {
        return () -> new CustomException("service  " + carId, HttpStatus.BAD_REQUEST);
    }

    public static Supplier<CustomException> bookingNotFound(Long bookingId){
        return () -> new CustomException("booking not found " + bookingId, HttpStatus.BAD_REQUEST);
    }
}
