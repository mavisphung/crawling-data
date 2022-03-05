package com.example.crawlingdata.crawlers;

import com.example.crawlingdata.repositories.JobRepository;
import com.example.crawlingdata.responses.models.JobItem;
import com.example.crawlingdata.util.TopCvData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TopCvSpider extends Crawler {
    private JobRepository jobRepo;

    public TopCvSpider(String keyword, String jobCategory, String companyField, int minSalary, int maxSalary, String location, int minimumExperience, int position, int workType) {
        super("https://www.topcv.vn", keyword, jobCategory, companyField, minSalary, maxSalary, location, minimumExperience, position, workType);
    }

    @Override
    public String formatUrl(String keyword, String location, String workType, String category, String companyField, String position, double fromSalary, double toSalary, int pageNum) {
        var keywordPart = "";
        var locationPart = "";
        var filterNotationsPart = "";
        Map<String, Object> filters = new HashMap<>();
        filters.put("1", null); // k <=> 1
        filters.put("2", null); // l <=> 2
        filters.put("3", null); // c <=> 3
        filters.put("4", null); // t <=> 4
        if (keyword != null && !keyword.isBlank()) {
            keywordPart = keyword.toLowerCase().replaceAll("\\s+", "-");
            filters.put("1", true);
        } else if (category != null && !category.isBlank()) {
            keywordPart = category.replaceAll("\\s+", "-");
        } else if (workType != null && workType.isBlank()) {
            switch (workType) {
                case "full-time": {
                    keywordPart = "toan-thoi-gian";
                    break;
                }
                case "part-time": {
                    keywordPart = "ban-thoi-gian";
                    break;
                }
                case "fresher": {
                    keywordPart = "thuc-tap";
                    break;
                }
            }
        }

        if (keywordPart == "") {
            keywordPart = "moi-nhat";
        }
        if (location != null) {
            var locationId = 1;
            locationPart = "-tai-" + location.replaceAll("\\s+", "-");
            filters.put("2", locationId);
        }
        if (category != null && !category.isBlank()) {
            var cateogryId = 10026;
            filters.put("3", cateogryId);
        }
        if (workType != null && !category.isBlank()) {
            var workTypeId = 1;
            filters.put("4", workTypeId);
        }

        if (filters.get("2") != null || filters.get("3") != null || filters.get("4") != null) {
            filterNotationsPart = "-";
        }

        for (String key : filters.keySet()) {
            if (key.equals("1")) {
                if (!filterNotationsPart.isBlank() && filters.get("1") != null) {
                    filterNotationsPart += "k";
                    System.out.println("KKKKK:  " + filterNotationsPart);
                }
            } else {
                if (filters.get(key) != null) {
                    switch (key) {
                        case "4":
                            filterNotationsPart += "t" + filters.get(key);
                            break;
                        case "2":
                            filterNotationsPart += "l" + filters.get(key);
                            break;
                        case "3":
                            filterNotationsPart += "c" + filters.get(key);
                            break;
                    }
                }
            }
            System.out.println(filterNotationsPart);
        }
        var url = "/tim-viec-lam-" + keywordPart + locationPart + filterNotationsPart;

        return url;
    }

    @Override
    public List<? extends JobItem> crawl() {
        String formatUrl = getBaseUrl() + formatUrl(super.getKeyword(), super.getLocation(), super.getWorkType() + "", super.getJobCategory(), super.getCompanyField(), super.getPosition() + "", getMinSalary(), getMaxSalary(), 1);
        System.out.println("Url: " + formatUrl);
        Document initDocument = null;
        Pattern numberPattern = Pattern.compile("[0-9]+");
        List<JobItem> jobItems = null;

        if (initDocument == null) {
            Document firstPageResult = null;
            try {
                firstPageResult = Jsoup.connect(formatUrl + "?page=0").get();

                Elements totalJobsElement = firstPageResult.body().select(TopCvData.JOBS_TOTAL);
                String text = totalJobsElement.first().text();

                int totalPage = 0;
                int totalJobs = 0;
                int counter = 1;
                if (numberPattern.matcher(text).matches()) {
                    totalJobs = Integer.parseInt(text);
                    totalPage = totalJobs % TopCvData.JOBS_IN_PAGE > 0 ? totalJobs / TopCvData.JOBS_IN_PAGE + 1 : totalJobs / TopCvData.JOBS_IN_PAGE;
                    while (counter < totalPage) {
                        Elements select = firstPageResult.body().select(".job-item");
                        for (Element e : select) {
                            String jobName = e.select(TopCvData.JOB_TITLE).text();
                            String companyName = e.select(TopCvData.COMPANY).text();
                            int remainDays = Integer.parseInt(e.select(TopCvData.DEADLINE).text());
                            String salary = e.select(TopCvData.SALARY).text();
                            String updateTime = e.select(TopCvData.TIME).text();
                            jobItems = jobItems == null ? new ArrayList<>() : jobItems;
                            jobItems.add(new JobItem(jobName, companyName, "", remainDays, getLocation(), salary, updateTime));
                            counter = counter + 1;
                            firstPageResult = Jsoup.connect(formatUrl + "?page=" + counter).get();
                        }
                    }
                    System.out.println("total jobs: " + jobItems.size());
                    return jobItems;

                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
