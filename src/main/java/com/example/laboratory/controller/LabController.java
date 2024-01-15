package com.example.laboratory.controller;

import com.example.laboratory.request.TestListRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/lab")
public class LabController {

    @GetMapping("/tests")
    public void getTests(@ModelAttribute TestListRequest testListRequest) {
        log.info(testListRequest.toString());
    }

}
