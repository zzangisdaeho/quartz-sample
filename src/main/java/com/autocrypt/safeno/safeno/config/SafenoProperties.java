package com.autocrypt.safeno.safeno.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;
import java.util.Map;


@ConfigurationProperties(prefix = "safeno")
@ToString
@Getter
public class SafenoProperties {

    private final Duration deleteTime;
    private final Map<ProviderEnum, Provider> provider;
    private final Map<ServiceEnum, Service> service;

    public SafenoProperties(@DefaultValue("7d") Duration deleteTime, Map<ProviderEnum, Provider> provider, Map<ServiceEnum, Service> service) {
        this.deleteTime = deleteTime;
        this.provider = provider;
        this.service = service;
    }

    @Getter
    @ToString
    public static class Provider {
        private final Duration deleteTime;
        private final String domain;

        public Provider(Duration deleteTime, String domain) {
            this.deleteTime = deleteTime;
            this.domain = domain;
        }
    }

    @Getter
    @ToString
    public static class Service {
        private final Duration deleteTime;
        private final ProviderEnum provider;

        public Service(Duration deleteTime, ProviderEnum provider) {
            this.deleteTime = deleteTime;
            this.provider = provider;
        }
    }

    public enum ProviderEnum {
        SKT, KT, LG
    }

    public enum ServiceEnum {
        TABOSO, MOVE_FREE, IMOM
    }
}
