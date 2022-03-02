package com.example.crawlingdata.crawlers;

import java.util.ArrayList;
import java.util.List;

import com.example.crawlingdata.repositories.JobRepository;
import com.example.crawlingdata.responses.models.JobItem;
import com.example.crawlingdata.util.TopCvData;
import com.example.crawlingdata.util.UrlBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TopCvSpider extends Crawler implements UrlBuilder {

    private int pageSizeTopCv;

    private JobRepository jobRepo;
    public TopCvSpider(JobRepository jobRepo) {
        this.jobRepo = jobRepo;
        this.pageSizeTopCv = 25;
        super.setBaseUrl("https://www.topcv.vn/tim-viec-lam-");
    }

    @Override
    public String formatUrl(
        String keyword, 
        String location, 
        String category, 
        String companyField,
        String position,
        double fromSalary, 
        double toSalary
    ) {
        // https://www.topcv.vn/tim-viec-lam-[java]-<tai-[ho-chi-minh]>-<kl2c[10026]t1>?[salary=3]&[company_field=33]&[position=1]&[page=0]
        //                                   keyword      location           category    salary      companyField       position
        StringBuilder sb = new StringBuilder(super.getBaseUrl());
        if (keyword != null && !keyword.isBlank())
            sb.append(genKeyword(keyword));

        if (location != null && !location.isBlank())
            sb.append(genLocation(location));
        
        if (category != null && !category.isBlank())
            sb.append(genCategory(category));
        
        super.setBaseUrl(sb.toString());
        return super.getBaseUrl();
    }

    @Override
    public List<? extends JobItem> crawl() {
        var jobs = new ArrayList<JobItem>();
        Document doc = null;
        int currentPage = 0;
        int jobsTotal = 0;
        try {
            doc = Jsoup.connect(super.getBaseUrl() + "?page=" + currentPage).get();
            jobsTotal = Integer.parseInt(doc.select(TopCvData.JOBS_TOTAL).first().text());
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            // e.printStackTrace();
            return null;
        }

        int pageTotal = jobsTotal % pageSizeTopCv == 0 ? jobsTotal / pageSizeTopCv : (jobsTotal / pageSizeTopCv) + 1;
        while (currentPage <= pageTotal) {
            Elements jobElements = doc.select(TopCvData.SEARCH_LIST);
            System.out.println(jobElements.size());
            if (jobElements.size() == 0) {
                return null;
            }
            for (var item : jobElements) {
                var logo = item.select(TopCvData.LOGO).first().attr("src"); // Get image of job item: logo
                var title = item.select(TopCvData.JOB_TITLE).first().text();
                var deadline = Integer.parseInt(item.select(TopCvData.DEADLINE).first().text());
                var company = item.select(TopCvData.COMPANY).first().text();
                var salary = item.select(TopCvData.SALARY).first().text();
                var location = item.select(TopCvData.LOCATION).first().text();
                var time = item.select(TopCvData.TIME).first().text();
                JobItem job = new JobItem(title, company, logo, deadline, location, salary, time);
                jobs.add(job);
                // System.out.println(img);
            }

            try {
                currentPage += 1;
                doc = Jsoup.connect(super.getBaseUrl() + "?page=" +currentPage).get();
                // jobsTotal = Integer.parseInt(doc.select(".search-job .list-job .job-header b").first().text());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        List<JobItem> results = jobRepo.saveAllAndFlush(jobs);

        System.out.println("Spider-Man crawled successfully");
        return results;
    }

    @Override
    public String genKeyword(String keyword) {
        // ads sad
        return keyword.replaceAll("\\s+", "-");
    }

    @Override
    public String genCategory(String category) {
        return "kl2c" + category + "t1";
    }

    @Override
    public String genLocation(String location) {
        return location.replaceAll("\\s+", "-");
    }


    @Override
    public String genSalary(long salaryRangeId) {
        // TODO Auto-generated method stub
        return null;
    }
    


}
