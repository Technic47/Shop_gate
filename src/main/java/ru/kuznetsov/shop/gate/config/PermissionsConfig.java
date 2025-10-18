package ru.kuznetsov.shop.gate.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@PropertySource("classpath:role-model.yml")
@ConfigurationProperties(prefix = "permissions")
public class PermissionsConfig {

    public final static String GET_PERMISSION = "get";
    public final static String SAVE_PERMISSION = "save";
    public final static String UPDATE_PERMISSION = "update";
    public final static String DELETE_PERMISSION = "delete";

    private Map<String, List<String>> accessRights;

    @Bean
    public List<String> gettingPermissions() {
        return accessRights.get(GET_PERMISSION);
    }

    @Bean
    public List<String> savingPermissions() {
        return accessRights.get(SAVE_PERMISSION);
    }

    @Bean
    public List<String> updatingPermissions() {
        return accessRights.get(UPDATE_PERMISSION);
    }

    @Bean
    public List<String> deletePermissions() {
        return accessRights.get(DELETE_PERMISSION);
    }

    public boolean hasAccess(String userRole, String permission) {
        return accessRights.get(permission).contains(userRole);
    }
}
