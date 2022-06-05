package ru.tigran.WebEditor.others;

import org.springframework.http.ResponseEntity;

public interface IResponseEntityConverter<T> {
    ResponseEntity<T> toResponseEntity();
}
