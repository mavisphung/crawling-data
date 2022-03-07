package com.example.crawlingdata.crawlers;

import com.example.crawlingdata.repositories.CategoryRepository;
import com.example.crawlingdata.repositories.CityRepository;
import com.example.crawlingdata.repositories.JobRepository;
import com.example.crawlingdata.repositories.PositionRepository;
import com.example.crawlingdata.repositories.SalaryRepository;
import com.example.crawlingdata.repositories.WorkTypeRepository;
import com.example.crawlingdata.responses.models.JobItem;
import com.example.crawlingdata.util.TopCvData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TopCvSpider extends Crawler {
    private JobRepository jobRepo;
    private CategoryRepository cateRepo;
    private CityRepository cityRepo;
    private PositionRepository positionRepo;
    private SalaryRepository salaryRepo;
    private WorkTypeRepository workTypeRepo;
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
            var formattedLocation = location.replaceAll("\\s+", "-");
            System.out.println(formattedLocation);
            var city = cityRepo.findByAliasContaining(formattedLocation);
            System.out.println("City: " + city.getAlias());
            locationPart = "-tai-" + city.getAlias().replaceAll("\\s+", "-");
            filters.put("2", city.getId());
        }
        if (category != null && !category.isBlank()) {
            var categoryObject = cateRepo.findById(Integer.parseInt(category));
            filters.put("3", categoryObject.get().getId());
            System.out.println("Category: " + categoryObject.get().getId());
        }
        if (workType != null && !workType.isBlank()) {
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
        // Pattern numberPattern = Pattern.compile("[0-9]+");
        List<JobItem> jobItems = new ArrayList<>();

        Document firstPageResult = null;
        try {
            firstPageResult = Jsoup.connect(formatUrl + "?page=0").get();

            Elements totalJobsElement = firstPageResult.body().select(TopCvData.JOBS_TOTAL);
            String text = totalJobsElement.first().text();
            int totalJobs = Integer.parseInt(text.replace(",", ""));
            int totalPage = 0;
            int counter = 1;
            totalPage = totalJobs % TopCvData.JOBS_IN_PAGE > 0 ? totalJobs / TopCvData.JOBS_IN_PAGE + 1 : totalJobs / TopCvData.JOBS_IN_PAGE;
            System.out.println("Total page: " + totalPage);
            System.out.println("-------------------------------------------------------------");
            while (counter <= totalPage) {
                Elements select = firstPageResult.body().select(".job-item");
                for (Element e : select) {
                    String jobName = e.select(TopCvData.JOB_TITLE).first().text();
                    String companyName = e.select(TopCvData.COMPANY).first().text();
                    String salary = e.select(TopCvData.SALARY).first().text();
                    String logo = e.select(TopCvData.LOGO).first().attr("src");
                    String location = e.select(TopCvData.LOCATION).first().text();
                    jobItems.add(new JobItem(jobName, companyName, logo, location, salary));
                }
                System.out.println("Crawling page: " + counter + " | " + "Jobs per page: " + select.size());
                counter = counter + 1;
                firstPageResult = Jsoup.connect(formatUrl + "?page=" + counter).get();
            }
            System.out.println("total jobs: " + jobItems.size());
            
            return jobRepo.saveAllAndFlush(jobItems);
            // return jobItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
