package com.niezhiliang.db.read.write.separate.dynamic.type.config;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/24 下午5:22
 */
public enum  DataSourceType {
    read("read", "从库"), write("write", "主库");

    private String type;

    private String name;

    DataSourceType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
