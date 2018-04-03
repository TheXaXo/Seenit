package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.exceptions.CommonException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class CustomErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value = ERROR_PATH)
    public void error(HttpServletResponse response) {
        int statusCode = response.getStatus();
        HttpStatus status = HttpStatus.valueOf(statusCode);

        String message = String.format("Status code: %s Reason: %s", status.value(), status.getReasonPhrase());

        throw new CommonException(message);
    }
}