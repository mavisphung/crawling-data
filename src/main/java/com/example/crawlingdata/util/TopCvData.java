package com.example.crawlingdata.util;

public class TopCvData {
    
    public static final String SEARCH_LIST = ".search-job .job-item";
    public static final String JOBS_TOTAL = ".search-job .list-job .job-header b";
    public static final String LOGO = ".avatar img";
    public static final String JOB_TITLE = ".body .title span";
    public static final String DEADLINE = ".body .content .deadline strong";
    public static final String COMPANY = ".body .company a";
    public static final String SALARY = ".body div.d-flex label.salary";
    public static final String LOCATION = ".body div.d-flex label.address";
    public static final String TIME = ".body div.d-flex label.time";

    public static final int JOBS_IN_PAGE = 25;
}
