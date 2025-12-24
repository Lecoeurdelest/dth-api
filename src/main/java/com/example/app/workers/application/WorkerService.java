package com.example.app.workers.application;

import com.example.app.auth.domain.Role;
import com.example.app.auth.domain.User;
import com.example.app.workers.dto.WorkerDto;
import com.example.app.workers.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final WorkerAvailabilityService workerAvailabilityService;

    public List<WorkerDto> getWorkers(String serviceType) {
        String skillFragment = (serviceType == null || serviceType.trim().isEmpty()) ? "" : serviceType.trim();
        List<User> workers;

        if (skillFragment.isEmpty()) {
            workers = workerRepository.findByRole(Role.WORKER);
        } else {
            // Try the custom query for better JSON handling
            workers = workerRepository.findWorkersBySkill(Role.WORKER, skillFragment);
            // If no results, fall back to simple text search
            if (workers.isEmpty()) {
                workers = workerRepository.findByRoleAndSkillsContaining(Role.WORKER, skillFragment);
            }
        }

        return workers.stream()
                .map(user -> {
                    WorkerDto dto = mapToWorkerDto(user);
                    // For general availability (no specific time), assume workers are available
                    dto.setAvailable(true);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Optional<WorkerDto> getWorkerById(Long id) {
        return workerRepository.findByIdAndRole(id, Role.WORKER)
                .map(this::mapToWorkerDto);
    }

    private WorkerDto mapToWorkerDto(User user) {
        return WorkerDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .skills(user.getSkills())
                .build();
    }

    /**
     * Get workers with availability status for a specific time slot
     *
     * @param serviceType service type filter (optional)
     * @param scheduledAt scheduled time (optional)
     * @param durationMinutes duration in minutes (optional)
     * @return list of workers with availability status
     */
    public List<WorkerDto> getWorkersWithAvailability(String serviceType, LocalDateTime scheduledAt, Integer durationMinutes) {
        String skillFragment = (serviceType == null || serviceType.trim().isEmpty()) ? "" : serviceType.trim();
        List<User> workers;

        if (skillFragment.isEmpty()) {
            workers = workerRepository.findByRole(Role.WORKER);
        } else {
            // Try the custom query for better JSON handling
            workers = workerRepository.findWorkersBySkill(Role.WORKER, skillFragment);
            // If no results, fall back to simple text search
            if (workers.isEmpty()) {
                workers = workerRepository.findByRoleAndSkillsContaining(Role.WORKER, skillFragment);
            }
        }

        return workers.stream()
                .map(user -> {
                    WorkerDto dto = mapToWorkerDto(user);
                    // Check availability for the specific time slot
                    boolean isAvailable = workerAvailabilityService.isWorkerAvailable(
                            user.getId(), scheduledAt, durationMinutes);
                    dto.setAvailable(isAvailable);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
