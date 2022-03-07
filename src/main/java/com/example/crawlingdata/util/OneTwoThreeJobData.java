package com.example.crawlingdata.util;

public class OneTwoThreeJobData {

    public static final String JOB_ITEMS = "#search__job .job__list .job__list-item";
    public static final String JOBS_TOTAL = "#search__job .job__top span.js-search-count";
    
    public static final String PAGINATION = "#pagination-listing";
    public static final int JOBS_PER_PAGE = 30;
    public static final int TOTAL_PAGE = 5;

    public static final String LOGO = ".job__list-item-thumb img";
    public static final String JOB_NAME = ".job__list-item-content .job__list-item-title a";
    public static final String COMPANY = ".job__list-item-content .job__list-item-company span";
    public static final String LOCATION = ".job__list-item-content .job__list-item-info .address span";
    public static final String SALARY = ".job__list-item-content .job__list-item-info div .salary";
}
