package com.autocrypt.safeno.safeno.api_client.config;

import com.autocrypt.safeno.safeno.api_client.SafenoClient;
import com.autocrypt.safeno.safeno.config.SafenoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

/**
 * 김대호
 * SafeNoClient 구현체들을 Map<SafenoProperties.ProviderEnum, SafeNoClient> 형태로 등록해주는 역할.
 */
@Configuration
@Slf4j
public class SafeNoClientMapRegister implements BeanPostProcessor, SmartInitializingSingleton {

    private final Map<SafenoProperties.ProviderEnum, SafenoClient> safeNoClientMap = new EnumMap<>(SafenoProperties.ProviderEnum.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // SafeNoClient 인터페이스를 구현한 빈들만 처리
        if (bean instanceof SafenoClient) {
            SafenoClient safeNoClient = (SafenoClient) bean;
            SafenoProperties.ProviderEnum providerEnum = safeNoClient.getProvider();
            
            // ProviderEnum과 SafeNoClient를 매핑하여 Map에 저장
            safeNoClientMap.put(providerEnum, safeNoClient);
            log.info("Registered SafeNoClient for provider: " + providerEnum);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    // SafeNoClient Map을 빈으로 등록
    @Bean
    public Map<SafenoProperties.ProviderEnum, SafenoClient> safeNoClientMap() {
        return safeNoClientMap;
    }

    // 모든 빈이 초기화된 후 호출
    @Override
    public void afterSingletonsInstantiated() {
        log.info("SafeNoClient Map initialized with size: " + safeNoClientMap.size());
    }
}