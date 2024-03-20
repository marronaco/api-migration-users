package com.eviden.migration.config;

import com.eviden.migration.service.MigrationService;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MigrationOnStartup implements ApplicationListener<ApplicationStartedEvent> {

    private final MigrationService migrationService;

    public MigrationOnStartup(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        migrationService.migracionUsuario();
    }
}
