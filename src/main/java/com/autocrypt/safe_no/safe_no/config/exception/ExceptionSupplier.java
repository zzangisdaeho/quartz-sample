package com.autocrypt.safe_no.safe_no.config.exception;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class ExceptionSupplier {

    public static Supplier<CustomException> propertyNotFound(Long carId) {
        return () -> new CustomException("service  " + carId, HttpStatus.BAD_REQUEST);
    }

    public static Supplier<CustomException> driveNotFound(String driveId){
        return () -> new CustomException("booking not found " + driveId, HttpStatus.BAD_REQUEST);
    }

    public static Supplier<CustomException> telNoNotFound(String telNo, String driveId){
        return () -> new CustomException(String.format("telNo %s not found in booking %s", telNo, driveId), HttpStatus.BAD_REQUEST);
    }
}
