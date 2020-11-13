package com.example.demo.util;


import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class Cache {

  private static final Gson gson = new Gson();
  @Autowired CacheManager cacheManager;

  public boolean put(String cacheName, String key, Object value) {
    log.info("putting data into cache; cacheName={} key={} value={}", cacheName, key, value);
    org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      cache.put(key, value);
      return true;
    } else {
      log.error("Error while storing data; Redis Cache returned null");
    }
    return false;
  }

  public boolean put(String cacheName, String key, Object value, boolean isComplex) {
    log.info("putting data into cache; cacheName={} key={} value={} isComplex={}", cacheName, key, value, isComplex);
    final String strValue = gson.toJson(value);
    org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      cache.put(key, strValue);
      return true;
    } else {
      log.error("Error while storing data; Redis Cache returned null");
    }
    return false;
  }

  public <T> T get(String cacheName, String key, Class<T> classOfT) {
    log.info("getting data from cache; cacheName={} key={}", cacheName, key);
    T cacheValue = null;
    if (cacheManager != null) {
      org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
      if (cache != null) {
        cacheValue = cache.get(key, classOfT);
        if (cacheValue != null) {
          log.info("loaded data from cache; key={}", key);
        } else {
          log.info("data cache missed; key={}", key);
        }
      } else {
        log.error("Redis Cache returned Null");
      }
    }
    return cacheValue;
  }
  public <T> T get(String cacheName, String key, Class<T> classOfT, boolean isComplex) {
    log.info("getting data from cache; cacheName={} key={} isComplex={}", cacheName, key, isComplex);
    T cacheValue = null;
    if (cacheManager != null) {
      org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
      if (cache != null) {
        String cacheValueAsString = cache.get(key, String.class);
        if (cacheValueAsString != null) {
          log.info("loaded data from cache; key={}", key);
          cacheValue = gson.fromJson(cacheValueAsString, classOfT);
        } else {
          log.info("data cache missed; key={}", key);
        }
      } else {
        log.error("Redis Cache returned Null");
      }
    }
    return cacheValue;
  }
}
