package com.jdum.commerce.sumysoul.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
    @NotEmpty(message = "Should not be empty")
    String login,
    @NotEmpty(message = "Should not be empty")
    String password
){

}
