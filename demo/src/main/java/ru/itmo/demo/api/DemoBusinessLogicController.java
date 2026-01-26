package ru.itmo.demo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import requests.WriteMessageRequest;
import ru.itmo.demo.service.DemoService;

/**
 * @author erik.karapetyan
 */
@RestController
@RequestMapping("/api/v1/bl")
@RequiredArgsConstructor
public class DemoBusinessLogicController {

    private final DemoService demoService;

    @PostMapping
    public void demoBl(@RequestBody WriteMessageRequest request, @RequestParam("synMode") boolean synMode) {
        demoService.someBl(request, synMode);
    }
}
