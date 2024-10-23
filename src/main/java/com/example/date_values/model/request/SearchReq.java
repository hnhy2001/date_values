package com.example.date_values.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchReq {
    private String filter;

    private Integer page;

    private Integer size;

    private String sort;
}
