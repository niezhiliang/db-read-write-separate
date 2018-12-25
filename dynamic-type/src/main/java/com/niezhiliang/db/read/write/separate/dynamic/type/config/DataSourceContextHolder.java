package com.niezhiliang.db.read.write.separate.dynamic.type.config;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/24 下午5:22
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> local = new ThreadLocal<String>();

    public static ThreadLocal<String> getLocal() {
        return local;
    }

    /**
     * 读可能是多个库
     */
    public static void read() {
        local.set(DataSourceType.read.getType());
    }

    /**
     * 写只有一个库
     */
    public static void write() {
        local.set(DataSourceType.write.getType());
    }

    public static String getJdbcType() {
        return local.get();
    }
}
