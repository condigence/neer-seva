package com.condigence.nsproductservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.condigence.nsproductservice.error.ApiErrorResponse;


@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Home", description = "Basic health and welcome endpoints for the product service")
public class HomeController {

    @Operation(summary = "Service root", description = "Returns a simple welcome message for the Neerseva application.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service is reachable"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping(value="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String home(){
        return "Welcome to our Neerseva Application!";
    }

    @Operation(summary = "Simple welcome", description = "Returns a short welcome string.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Welcome message returned"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping(value="/welcome", produces = MediaType.APPLICATION_JSON_VALUE)
    public String welcome(){
        return "WELCOME!";
    }


}
