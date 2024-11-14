package com.aims.datamodel.sdk;

import com.aims.datamodel.sdk.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${data-model.config-dir:./datamodel-configs}")
    private String CONFIG_DIR;
    /**
     * 数据模型子目录
     */
    private String DATA_MODEL_DIR;
    /**
     * 数据库配置子目录
     */
    private String DATABASE_DIR;

    public String getDATA_MODEL_DIR() {
        if (DATA_MODEL_DIR == null) DATA_MODEL_DIR = FileUtil.buildPath(CONFIG_DIR, "data-models");
        return DATA_MODEL_DIR;
    }

    public String getDATABASE_DIR() {
        if (DATABASE_DIR == null) DATABASE_DIR = FileUtil.buildPath(CONFIG_DIR, "databases");
        return DATABASE_DIR;
    }
}
