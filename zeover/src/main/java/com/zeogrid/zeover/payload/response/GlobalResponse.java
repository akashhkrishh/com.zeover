package com.zeogrid.zeover.payload.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse<T> {
    T data;
    String message;
    String error;
    boolean success;
}
