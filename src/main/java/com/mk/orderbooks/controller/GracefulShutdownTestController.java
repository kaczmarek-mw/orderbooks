package com.mk.orderbooks.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/graceful-shutdown")
@Api(value = "/graceful-shutdown", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GracefulShutdownTestController {

    @GetMapping("/test")
    @ApiOperation(
            value = "Blocks the thread for 15 seconds. Kill PID during this time window to observe graceful shutdown.",
            response = String.class)
    public String pause() throws InterruptedException {
        Thread.sleep(15000);
        return "Process finished after 15 seconds";
    }
}
