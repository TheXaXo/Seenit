package com.thexaxo.seenit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid comment content.")
public class InvalidCommentContentException extends RuntimeException {
}