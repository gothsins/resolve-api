package com.gothsins.resolve.config;

import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.boot.health.contributor.Health;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HazelcastHealthIndicator implements HealthIndicator {

    private final HazelcastInstance hazelcastInstance;

    @Override
    public Health health() {
        if (hazelcastInstance.getLifecycleService().isRunning()) {
            return Health.up()
                    .withDetail("cluster", hazelcastInstance.getConfig().getClusterName())
                    .withDetail("members", hazelcastInstance.getCluster().getMembers().size())
                    .build();
        }
        return Health.down().build();
    }
}