package com.tiangou.interceptor;

public class ShardTableHelper {

    private final static ThreadLocal<String> LOCAL_TABLE_NAME = new ThreadLocal<String>();


    public static void startShardingTable(String suffixName) {
        LOCAL_TABLE_NAME.set(suffixName);
    }

    protected static String getSuffixName() {
        String suffixName = LOCAL_TABLE_NAME.get();
        LOCAL_TABLE_NAME.remove();
        return suffixName;
    }

}
