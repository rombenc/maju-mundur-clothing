package com.majumundur.clothing;

import com.majumundur.clothing.entity.Role;
import com.majumundur.clothing.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableAsync
public class MajuMundurApplication {

    public static void main(String[] args) {
        SpringApplication.run(MajuMundurApplication.class, args);
    }
    @Bean
    public CommandLineRunner runner(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("CUSTOMER").isEmpty()) {
                roleRepository.save(Role.builder().name("CUSTOMER").createdDate(LocalDateTime.now()).build());
            }
            if (roleRepository.findByName("MERCHANT").isEmpty()) {
                roleRepository.save(Role.builder().name("MERCHANT").createdDate(LocalDateTime.now()).build());
            }
        };
    }
}
