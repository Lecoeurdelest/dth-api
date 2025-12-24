package com.example.app.auth.config;

import com.example.app.auth.domain.Role;
import com.example.app.auth.domain.User;
import com.example.app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile({"dev","docker","!prod"})
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create some worker users if they don't exist
        if (userRepository.existsByUsername("worker.electric")) return;

        User w1 = User.builder()
                .email("electro@example.com")
                .username("worker.electric")
                .firstName("Anh")
                .lastName("Thợ Điện")
                .phone("0900000001")
                .password(passwordEncoder.encode("A123456a"))
                .role(Role.WORKER)
                .skills("[\"Sửa điện tại nhà\",\"SỬA CHỮA ĐIỆN\"]")
                .build();

        User w2 = User.builder()
                .email("plumber@example.com")
                .username("worker.plumber")
                .firstName("Bình")
                .lastName("Thợ Nước")
                .phone("0900000002")
                .password(passwordEncoder.encode("A123456a"))
                .role(Role.WORKER)
                .skills("[\"Sửa nước tại nhà\",\"SỬA CHỮA NƯỚC\"]")
                .build();

        User w3 = User.builder()
                .email("carpenter@example.com")
                .username("worker.carpenter")
                .firstName("Cường")
                .lastName("Thợ Mộc")
                .phone("0900000003")
                .password(passwordEncoder.encode("A123456a"))
                .role(Role.WORKER)
                .skills("[\"Sửa chữa đồ mộc\",\"SỬA CHỮA ĐỒ MỘC\"]")
                .build();

        userRepository.saveAll(List.of(w1, w2, w3));
    }
}



