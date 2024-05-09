package com.aims.datamodel.ide.controller.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ApiError {
    private int code;
    private String msg;
    private Object detail;
}
