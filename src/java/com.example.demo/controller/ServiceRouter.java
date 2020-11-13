package com.example.demo.controller;

import com.example.demo.response.DummyResponse;
import com.example.demo.response.MarkDownDiscountResponse;
import com.example.demo.service.DummyService;
import com.example.demo.service.MarkDownDiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;

@RestController
@Slf4j
public class ServiceRouter {
    @Autowired
    DummyService dummyService;

    @Autowired
    MarkDownDiscountService markDownDiscountService;

    @GetMapping("/dummy-service/test/v1")
    DummyResponse restTest(@QueryParam("eventID") String eventID) {
        log.info("####eventID={}", eventID);
        return dummyService.getDummyResp(eventID);
    }

    @GetMapping("/dummy-markdown-service/test/v1")
    MarkDownDiscountResponse markDownTest(
            @QueryParam("eventID") String eventID,
            @QueryParam("bucketID") String bucketID) {
        log.info("####eventID={}, bucketID={} ", eventID, bucketID);

        return markDownDiscountService.getMarkDownResp(eventID, bucketID);
    }
}
