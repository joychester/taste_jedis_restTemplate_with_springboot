package com.example.demo.service;

import com.example.demo.response.DummyResponse;
import com.example.demo.util.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class DummyService {
    @Autowired
    private Cache cache;

    private static final String EVENT_CACHE = "eventCache";

    public DummyResponse getDummyResp(String eventID) {
        DummyResponse resp = new DummyResponse();

        // fetch eventID from cache first
        String eventNameFromCache = cache.get(EVENT_CACHE, eventID, String.class);

        if( eventNameFromCache != null) {
            resp.setDummyResponse(eventNameFromCache);
        } else {
            //should call the dummy service API first and put resp into cache for later use
            resp.setDummyResponse("Dummy Event 123 from API");
            cache.put(EVENT_CACHE, eventID, "Dummy Event 123 from Redis");
        }
        return resp;
    }


    /*
    @PostConstruct
    public void setupTest() {
        getDummyResp("12345"); // should get from API response
        getDummyResp("12345"); // should get from cache
        getDummyResp("12345"); // should get from cache
    }
    */

}
