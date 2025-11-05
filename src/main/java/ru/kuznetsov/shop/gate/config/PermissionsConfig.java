package ru.kuznetsov.shop.gate.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.gate.enums.UserPermissionEnum;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@PropertySource(value = "classpath:role-model.yaml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "permissions")
public class PermissionsConfig {

    private Map<UserPermissionEnum, List<String>> apiMethods;

    public boolean hasAccess(String userRole, UserPermissionEnum permission) {
        return apiMethods.get(permission).contains(userRole);
    }
}
