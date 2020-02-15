package com.psa.ranking.config;

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
            createCache(cm, com.psa.ranking.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.psa.ranking.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.psa.ranking.domain.User.class.getName());
            createCache(cm, com.psa.ranking.domain.Authority.class.getName());
            createCache(cm, com.psa.ranking.domain.User.class.getName() + ".authorities");
            createCache(cm, com.psa.ranking.domain.Country.class.getName());
            createCache(cm, com.psa.ranking.domain.Country.class.getName() + ".provinces");
            createCache(cm, com.psa.ranking.domain.Province.class.getName());
            createCache(cm, com.psa.ranking.domain.Province.class.getName() + ".cities");
            createCache(cm, com.psa.ranking.domain.City.class.getName());
            createCache(cm, com.psa.ranking.domain.DocType.class.getName());
            createCache(cm, com.psa.ranking.domain.Tournament.class.getName());
            createCache(cm, com.psa.ranking.domain.Tournament.class.getName() + ".events");
            createCache(cm, com.psa.ranking.domain.Event.class.getName());
            createCache(cm, com.psa.ranking.domain.Event.class.getName() + ".categories");
            createCache(cm, com.psa.ranking.domain.Team.class.getName());
            createCache(cm, com.psa.ranking.domain.Category.class.getName());
            createCache(cm, com.psa.ranking.domain.Category.class.getName() + ".events");
            createCache(cm, com.psa.ranking.domain.Roster.class.getName());
            createCache(cm, com.psa.ranking.domain.Roster.class.getName() + ".players");
            createCache(cm, com.psa.ranking.domain.Player.class.getName());
            createCache(cm, com.psa.ranking.domain.Player.class.getName() + ".rosters");
            createCache(cm, com.psa.ranking.domain.UserExtra.class.getName());
            createCache(cm, com.psa.ranking.domain.UserExtra.class.getName() + ".cities");
            createCache(cm, com.psa.ranking.domain.Format.class.getName());
            createCache(cm, com.psa.ranking.domain.PlayerPoint.class.getName());
            createCache(cm, com.psa.ranking.domain.PlayerDetailPoint.class.getName());
            createCache(cm, com.psa.ranking.domain.TeamPoint.class.getName());
            createCache(cm, com.psa.ranking.domain.TeamDetailPoint.class.getName());
            createCache(cm, com.psa.ranking.domain.EventCategory.class.getName());
            createCache(cm, com.psa.ranking.domain.Fixture.class.getName());
            createCache(cm, com.psa.ranking.domain.Game.class.getName());
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
