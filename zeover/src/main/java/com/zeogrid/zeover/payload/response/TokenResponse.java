package com.zeogrid.zeover.payload.response;

public record TokenResponse(
        String token,
        String email
) {}
