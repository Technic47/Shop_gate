package ru.kuznetsov.shop.gate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PermissionsConfig.class)
public class GateConfig {
}
