package com.ar.pbpoints.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.ar.pbpoints.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.ar.pbpoints.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.ar.pbpoints.domain.User.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Authority.class.getName());
            createCache(cm, com.ar.pbpoints.domain.User.class.getName() + ".authorities");
            createCache(cm, com.ar.pbpoints.domain.Country.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Country.class.getName() + ".provinces");
            createCache(cm, com.ar.pbpoints.domain.Province.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Province.class.getName() + ".cities");
            createCache(cm, com.ar.pbpoints.domain.City.class.getName());
            createCache(cm, com.ar.pbpoints.domain.DocType.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Tournament.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Tournament.class.getName() + ".events");
            createCache(cm, com.ar.pbpoints.domain.Event.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Event.class.getName() + ".categories");
            createCache(cm, com.ar.pbpoints.domain.Team.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Category.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Category.class.getName() + ".events");
            createCache(cm, com.ar.pbpoints.domain.Roster.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Roster.class.getName() + ".players");
            createCache(cm, com.ar.pbpoints.domain.Player.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Player.class.getName() + ".rosters");
            createCache(cm, com.ar.pbpoints.domain.UserExtra.class.getName());
            createCache(cm, com.ar.pbpoints.domain.UserExtra.class.getName() + ".cities");
            createCache(cm, com.ar.pbpoints.domain.Format.class.getName());
            createCache(cm, com.ar.pbpoints.domain.PlayerPoint.class.getName());
            createCache(cm, com.ar.pbpoints.domain.PlayerDetailPoint.class.getName());
            createCache(cm, com.ar.pbpoints.domain.TeamPoint.class.getName());
            createCache(cm, com.ar.pbpoints.domain.TeamDetailPoint.class.getName());
            createCache(cm, com.ar.pbpoints.domain.EventCategory.class.getName());
            createCache(cm, com.ar.pbpoints.domain.Game.class.getName());
            createCache(cm, com.ar.pbpoints.domain.EventCategory.class.getName() + ".games");
            createCache(cm, com.ar.pbpoints.domain.Bracket.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
