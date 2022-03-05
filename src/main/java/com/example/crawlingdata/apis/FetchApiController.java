package com.example.crawlingdata.apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.crawlingdata.crawlers.TopCvSpider;
import com.example.crawlingdata.repositories.JobRepository;
import com.example.crawlingdata.responses.JobResponse;
import com.example.crawlingdata.responses.models.JobItem;
import com.example.crawlingdata.responses.models.WikiItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class FetchApiController {
    
    private final String TOPCV = "https://www.topcv.vn";
    List<? extends JobItem> jobList;
    private int jobsTotal = 0;
    private int pageSizeTopCv = 25;
    private WebDriver web;
    private final JobRepository jobRepo;

    public FetchApiController(WebDriver web, JobRepository jobRepo) {
        this.web = web;
        jobList = new ArrayList<JobItem>();
        this.jobRepo = jobRepo;
    }

    @GetMapping(value = {"", "/"})
    public String fetch() {
        
        try {
            web.get(TOPCV);
            var keywordInput = web.findElement(By.cssSelector("input#keyword"));
            var submitButton = web.findElement(By.cssSelector(".btn-search-job"));
    //        WebElement workCategory = web.findElement(By.cssSelector("span#select2-category-container"));
    //        WebElement categorySelect = web.findElement(By.cssSelector("select#category"));
            keywordInput.sendKeys("java developer");
            submitButton.click();
            
            List<WebElement> elements = web.findElements(By.cssSelector("a.underline-box-job"));
            elements.stream().forEach((ele) ->{
                System.out.println(ele.getAttribute("href"));
            });

        } catch (Exception e) {
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
        }
        return "Hello world";
    }

    @GetMapping("/wiki/")
    public ResponseEntity<ArrayList<WikiItem>> fetchDataFromWiki() {
        ArrayList<WikiItem> results = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect("https://en.wikipedia.org/").get();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Title: " + doc.title());
        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            var item = new WikiItem(headline.attr("title"), headline.absUrl("href"));
            results.add(item);
            System.out.println(item.getTitle() + "\n\t" + item.getHref());
        }
        return ResponseEntity.ok(results);
    }

    //https://www.topcv.vn/tim-viec-lam-fs?page=0
    @GetMapping("/find-job")
    public ResponseEntity<Object> findJob(
        @RequestParam String url, 
        @RequestParam String jobName,
        @RequestParam(required = false) int page
    ) {
        System.out.println("findJob invoked");
        jobList = crawlJobsFromTopCv(url, jobName, page);
        JobResponse response = new JobResponse();
        
        if (jobList == null || jobList.size() == 0) {
            response.setStatus(200);
            response.setMessage("No data for the query");
        } 
        else {
            response.setStatus(200);
            response.setMessage("Get successfully");
            int totalPage = jobsTotal / pageSizeTopCv;
            response.setTotal(totalPage % 2 == 0 ? totalPage : totalPage + 1);
            response.setNext(getUrl(url) + jobName + "?page=" + (page + 1));
            response.setData(jobList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-job-v2")
    public ResponseEntity<Object> findAllJobs(
        @RequestParam String url, 
        @RequestParam String jobName
    ) {
        System.out.println("findJob invoked");
        List<JobItem> jobList = getAllJobsFrom(url, jobName);
        JobResponse response = new JobResponse();
        
        if (jobList == null || jobList.size() == 0) {
            response.setStatus(200);
            response.setMessage("No data for the query");
        } 
        else {
            response.setStatus(200);
            response.setMessage("Get successfully");
            int totalPage = jobsTotal / pageSizeTopCv;
            response.setTotal(totalPage % 2 == 0 ? totalPage : totalPage + 1);
            response.setData(jobList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-job-v3")
    public ResponseEntity<Object> findAllJobsV3(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String workType,
        @RequestParam(required = false) String companyField,
        @RequestParam(required = false) String position,
        @RequestParam(required = false) String salary
    ) {

        TopCvSpider spider = new TopCvSpider(keyword,category,companyField,0,100000,location,2,1,1);
        List<? extends JobItem> crawl = spider.crawl();
        return ResponseEntity.ok(crawl);
    }

    private ArrayList<JobItem> crawlJobsFromTopCv(String url, String jobName, int page) {
        var fullUrl = getUrl(url);
        Document doc = null;
        
        try {
            doc = Jsoup.connect(fullUrl + jobName + "?page=" + page).get();
            jobsTotal = Integer.parseInt(doc.select(".search-job .list-job .job-header b").first().text());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        Elements jobElements = doc.select(".search-job .job-item");
        System.out.println(jobElements.size());
        if (jobElements.size() == 0) {
            return null;
        }
        ArrayList<JobItem> jobs = new ArrayList<JobItem>();
        for (var item : jobElements) {
            var logo = item.select(".avatar img").first().attr("src"); // Get image of job item: logo
            var title = item.select(".body .title span").first().text();
            var deadline = Integer.parseInt(item.select(".body .content .deadline strong").first().text());
            var company = item.select(".body .company a").first().text();
            var salary = item.select(".body div.d-flex label.salary").first().text();
            var location = item.select(".body div.d-flex label.address").first().text();
            var time = item.select(".body div.d-flex label.time").first().text();
            JobItem job = new JobItem(title, company, logo, deadline, location, salary, time);
            jobs.add(job);
            // System.out.println(img);
        }
        return jobs;
    }

    // @Transactional
    private List<JobItem> getAllJobsFrom(String url, String jobName) {
        var fullUrl = getUrl(url);
        var jobs = new ArrayList<JobItem>();
        Document doc = null;
        int currentPage = 0;
        try {
            doc = Jsoup.connect(fullUrl + jobName + "?page=" + currentPage).get();
            jobsTotal = Integer.parseInt(doc.select(".search-job .list-job .job-header b").first().text());
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            // e.printStackTrace();
            return null;
        }
        int pageTotal = jobsTotal / pageSizeTopCv;
        while (currentPage <= pageTotal) {
            Elements jobElements = doc.select(".search-job .job-item");
            System.out.println(jobElements.size());
            if (jobElements.size() == 0) {
                return null;
            }
            for (var item : jobElements) {
                var logo = item.select(".avatar img").first().attr("src"); // Get image of job item: logo
                var title = item.select(".body .title span").first().text();
                var deadline = Integer.parseInt(item.select(".body .content .deadline strong").first().text());
                var company = item.select(".body .company a").first().text();
                var salary = item.select(".body div.d-flex label.salary").first().text();
                var location = item.select(".body div.d-flex label.address").first().text();
                var time = item.select(".body div.d-flex label.time").first().text();
                JobItem job = new JobItem(title, company, logo, deadline, location, salary, time);
                jobs.add(job);
                // System.out.println(img);
            }

            try {
                currentPage += 1;
                doc = Jsoup.connect(fullUrl + jobName + "?page=" +currentPage).get();
                // jobsTotal = Integer.parseInt(doc.select(".search-job .list-job .job-header b").first().text());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        List<JobItem> results = jobRepo.saveAllAndFlush(jobs);

        return results;
    }
    private String getUrl(String url) {
        StringBuilder sb = new StringBuilder("https://www.");
        if (url.contains("topcv")) {
            sb.append(url + "/tim-viec-lam-");
        }
        else {
            sb.append("topcv.vn");
        }
        return sb.toString();
    }
}
