package com.gothsins.resolve.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import io.github.bucket4j.grid.hazelcast.Bucket4jHazelcast;
import io.github.bucket4j.grid.hazelcast.HazelcastProxyManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hazelcast.core.Hazelcast;

@Configuration
public class HazelcastConfig {

    @Bean
    public Config hazelcastConfiguration() {
        Config config = new Config();

        config.setClusterName("resolve-cluster");

        config.addMapConfig(
                new MapConfig("rate-limit-buckets")
                        .setBackupCount(1)
        );

        return config;
    }
    @Bean
    public HazelcastInstance hazelcastInstance(Config hazelcastConfiguration) {
        return Hazelcast.newHazelcastInstance(hazelcastConfiguration);
    }

    @Bean
    public IMap<String, byte[]> rateLimitMap(
            HazelcastInstance hazelcastInstance
    ) {
        return hazelcastInstance.getMap("rate-limit-buckets");
    }

    @Bean
    public HazelcastProxyManager<String> proxyManager(
            IMap<String, byte[]> rateLimitMap
    ) {
        return Bucket4jHazelcast
                .entryProcessorBasedBuilder(rateLimitMap)
                .build();
    }
}