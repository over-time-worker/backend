package com.owlexpress.hub.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub")
public class HubController {

    @GetMapping
    public void find() {
    }

    @PostMapping
    public void create() {
    }

    @PutMapping
    public void update() {
    }

    @DeleteMapping
    public void delete() {
    }

}
