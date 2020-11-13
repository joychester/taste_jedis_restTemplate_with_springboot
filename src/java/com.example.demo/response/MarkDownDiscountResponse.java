package com.example.demo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.Calendar;


@JsonRootName(value = "MarkDownDiscountResponse")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MarkDownDiscountResponse {
    private String entityId;
    private String entityType;
    private String discountTypeId;
    private String description;
    private Boolean stackable;
    private Calendar expiryDate;
    private Boolean active;
    private String promotionalCodeId;
    private String codePrefix;
    private String codeSuffix;
}
