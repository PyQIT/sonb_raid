package com.example.sonb_raid.API;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/raid0")
public class Raid0Controller {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void getRaid0(){

    }
}