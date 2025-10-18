package ru.kuznetsov.shop.gate.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.gate.listener.SaveOperationListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SaveOperationCleanUpScheduler {

    private final SaveOperationListener saveOperationListener;

    @Scheduled(fixedRate = 100000)
    public void cleanup() {

        List<String> oldOperations = getOldOperations(saveOperationListener.getSuccessfulOperationsWithTime());

        saveOperationListener.removeSuccessfulOperations(oldOperations);

        oldOperations = getOldOperations(saveOperationListener.getFailedOperationsWithTime());

        saveOperationListener.removeFailedOperations(oldOperations);
    }

    private List<String> getOldOperations(Map<String, List<LocalDateTime>> operationsMap) {
        List<String> oldOperations = new ArrayList<>();

        for (Map.Entry<String, List<LocalDateTime>> entry : operationsMap.entrySet()) {
            entry.getValue().stream()
                    .max(LocalDateTime::compareTo)
                    .ifPresent(localDateTime -> {
                        if (localDateTime.isBefore(LocalDateTime.now().minusHours(1))) {
                            oldOperations.add(entry.getKey());
                        }
                    });
        }

        return oldOperations;
    }
}
