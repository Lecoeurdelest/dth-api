package com.example.app.services.application;

import com.example.app.services.dto.ServiceDto;
import com.example.app.services.mapper.ServiceMapper;
import com.example.app.services.repository.ServiceRepository;
import com.example.app.shared.exception.ResourceNotFoundException;
import com.example.app.shared.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ServiceApplicationService
 * 
 * <p>This is the internal implementation. Other modules should use the interface,
 * not this class directly.
 */
@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceApplicationService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Override
    @Transactional(readOnly = true)
    public PageUtil.PageResponse<ServiceDto> getAllServices(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageUtil.createPageable(page, size, sortBy, sortDir);
        Page<com.example.app.services.domain.Service> services = serviceRepository.findByActiveTrue(pageable);
        return PageUtil.toPageResponse(services.map(s -> serviceMapper.toDto(s)));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDto getServiceById(Long id) {
        return serviceRepository.findByIdAndActiveTrue(id)
                .map(serviceMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDto> getServicesByCategory(String category) {
        return serviceRepository.findByCategory(category).stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateServiceRating(Long serviceId, Double newRating, Long reviewCount) {
        com.example.app.services.domain.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));
        // Update rating fields if they exist in the entity
        // This is a placeholder - implement based on your Service entity structure
        serviceRepository.save(service);
    }
}

