package com.gothsins.resolve.service;

import com.hazelcast.map.IMap;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.grid.hazelcast.Bucket4jHazelcast;
import io.github.bucket4j.grid.hazelcast.HazelcastProxyManager;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final HazelcastProxyManager<String> proxyManager;

    private final BucketConfiguration userCreationConfig =
            BucketConfiguration.builder()
                    .addLimit(limit -> limit
                            .capacity(5)
                            .refillGreedy(5, Duration.ofMinutes(1)))
                    .build();

    public RateLimitService(IMap<String, byte[]> rateLimitMap) {
        this.proxyManager = Bucket4jHazelcast
                .entryProcessorBasedBuilder(rateLimitMap)
                .build();
    }

    public boolean allowUserCreation(String key) {
        Bucket bucket = proxyManager.getProxy(
                "user-create:" + key,
                () -> userCreationConfig
        );

        return bucket.tryConsume(1);
    }
}