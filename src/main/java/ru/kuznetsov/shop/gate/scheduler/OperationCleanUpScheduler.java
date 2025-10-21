package ru.kuznetsov.shop.gate.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.gate.service.OperationService;

@Component
@RequiredArgsConstructor
public class OperationCleanUpScheduler {

    private final OperationService operationService;

    @Scheduled(fixedRate = 100000)
    public void cleanup() {
        operationService.removeOldOperations();
    }
}
