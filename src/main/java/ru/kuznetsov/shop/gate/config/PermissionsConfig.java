package ru.kuznetsov.shop.gate.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.gate.enums.UserPermissionEnum;

import java.util.List;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@PropertySource(value = "classpath:role-model.yaml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "permissions")
public class PermissionsConfig {

    private List<String> get;
    private List<String> save;
    private List<String> update;
    private List<String> delete;

    @Bean
    public List<String> gettingPermissions() {
        return get;
    }

    @Bean
    public List<String> savingPermissions() {
        return save;
    }

    @Bean
    public List<String> updatingPermissions() {
        return update;
    }

    @Bean
    public List<String> deletePermissions() {
        return delete;
    }

    public boolean hasAccess(String userRole, UserPermissionEnum permission) {
        return switch (permission) {
            case GET -> get.contains(userRole);
            case SAVE -> save.contains(userRole);
            case UPDATE -> update.contains(userRole);
            case DELETE -> delete.contains(userRole);
        };
    }
}
