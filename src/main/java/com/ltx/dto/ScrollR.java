package com.ltx.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScrollR {
    private List<?> list;
    private Long minTime;
    private Integer offset;
}
