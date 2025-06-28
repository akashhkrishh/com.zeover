package com.zeogrid.zeover.service;

import com.zeogrid.zeover.payload.response.GlobalResponse;
import com.zeogrid.zeover.payload.response.WelcomeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WelcomeService {

    public ResponseEntity<GlobalResponse<WelcomeResponse>> welcome() {
        WelcomeResponse welcomeResponse = new WelcomeResponse("Hello broskey!");
        GlobalResponse<WelcomeResponse> globalResponse = new GlobalResponse<>(welcomeResponse,"Message get successfully!",null,false);
        return ResponseEntity.ok(globalResponse);
    }
}
