package com.example.app.auth.api;

import com.example.app.auth.dto.WorkerDto;
import com.example.app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<WorkerDto>> getWorkersByService(@RequestParam(required = false) String service) {
        String fragment = (service == null) ? "" : service;
        List<com.example.app.auth.domain.User> users = userRepository.findByRoleAndSkillsContaining("WORKER", fragment);
        List<WorkerDto> dtos = users.stream().map(u -> WorkerDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .phone(u.getPhone())
                .avatarUrl(u.getAvatarUrl())
                .skills(u.getSkills())
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}



