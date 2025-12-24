package com.example.app.workers.api;

import com.example.app.workers.application.WorkerService;
import com.example.app.workers.dto.WorkerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping
    public ResponseEntity<List<WorkerDto>> getWorkers(@RequestParam(required = false) String service) {
        List<WorkerDto> workers = workerService.getWorkers(service);
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkerDto> getWorkerById(@PathVariable Long id) {
        Optional<WorkerDto> worker = workerService.getWorkerById(id);
        return worker.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
}



