package com.example.til.exception;

public class TilNotFoundException extends RuntimeException {

    public TilNotFoundException(Long id) {
        super("id " + id + "에 해당하는 TIL이 없습니다");
    }
}
