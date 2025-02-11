package com.poniansoft.shrtly.test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/app")
public class TestContoller {
    @RequestMapping("/landing")
    public String test() {
        return "Test";
    }
}
