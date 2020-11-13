package com.example.demo.service;

import com.example.demo.response.MarkDownDiscountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MarkDownDiscountService {

    @Autowired
    RestTemplate restTemplate;

    public MarkDownDiscountResponse getMarkDownResp(String eventID, String bucketID) {
        final StopWatch sw = new StopWatch();
        sw.start();

        final String markDownMockUrl = "http://localhost:8020/";
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer MOCK_APP_TOKEN");

        MarkDownDiscountResponse resp = null;

        resp = restTemplate.exchange(
                    markDownMockUrl,
                    HttpMethod.GET,
                    new HttpEntity<>("", headers),
                    MarkDownDiscountResponse.class)
                .getBody();

        sw.stop();
        log.debug("####markDownresponse={} , respTime={}", resp, sw.getLastTaskTimeMillis());
        log.info("respTime={}", sw.getLastTaskTimeMillis());

        return resp;
    }

}
