package com.aims.datamodel.sdk.entity;

import lombok.Data;

@Data
public class DataModelEntity {
    private String id;
    private String name;
    private String version;
    private String type;
    private String module;
    private String configJson;
    private String memo;
}
