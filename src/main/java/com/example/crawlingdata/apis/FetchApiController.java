package com.example.crawlingdata.apis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class FetchApiController {
    

    @GetMapping(value = {"", "/"})
    public String fetch() {
        return "Hello world";
    }

}
