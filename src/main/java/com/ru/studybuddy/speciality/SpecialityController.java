package com.ru.studybuddy.speciality;

import com.ru.studybuddy.speciality.requests.CreateSpecialityRequest;
import com.ru.studybuddy.speciality.responses.CreateSpecialityResponse;
import com.ru.studybuddy.speciality.responses.GetSpecialitiesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/specialties")
public class SpecialityController {

    private final SpecialityService service;

    @PostMapping("/create")
    public ResponseEntity<CreateSpecialityResponse> create(CreateSpecialityRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/get")
    public ResponseEntity<GetSpecialitiesResponse> get() {
        return ResponseEntity.ok(service.get());
    }
}