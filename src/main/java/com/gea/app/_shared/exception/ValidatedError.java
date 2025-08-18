package com.gea.app._shared.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidatedError {
    String field;
    String issue;
}
