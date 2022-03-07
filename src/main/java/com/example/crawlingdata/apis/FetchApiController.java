package com.example.crawlingdata.apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.crawlingdata.crawlers.OneTwoThreeJobSpider;
import com.example.crawlingdata.crawlers.TopCvSpider;
import com.example.crawlingdata.repositories.*;
import com.example.crawlingdata.responses.JobResponse;
import com.example.crawlingdata.responses.models.JobItem;
import com.example.crawlingdata.responses.models.WikiItem;
import com.example.crawlingdata.util.DummyDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class FetchApiController {
    
    List<? extends JobItem> jobList;
    private WebDriver web;
    private final JobRepository jobRepo;
    private final CategoryRepository cateRepo;
    private final CityRepository cityRepo;
    private final PositionRepository positionRepo;
    private final SalaryRepository salaryRepo;
    private final WorkTypeRepository workTypeRepo;
    private DummyDatabase db;

    public FetchApiController(
        WebDriver web, 
        JobRepository jobRepo, 
        CategoryRepository cateRepo,
        CityRepository cityRepo,
        PositionRepository positionRepo,
        SalaryRepository salaryRepo,
        WorkTypeRepository workTypeRepo
    ) {
        this.web = web;
        jobList = new ArrayList<JobItem>();
        this.jobRepo = jobRepo;
        this.cateRepo = cateRepo;
        this.cityRepo = cityRepo;
        this.positionRepo = positionRepo;
        this.salaryRepo = salaryRepo;
        this.workTypeRepo = workTypeRepo;
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

    @GetMapping("/find-job-v3")
    public ResponseEntity<JobResponse> findAllJobsV3(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String workType,
        @RequestParam(required = false) String companyField,
        @RequestParam(required = false) String position,
        @RequestParam(required = false) String salary
    ) {
        
        TopCvSpider spider = new TopCvSpider(keyword, category, companyField, 0, 100000, location, 2, 1, 1);
        spider.setJobRepo(jobRepo);
        spider.setCateRepo(cateRepo);
        spider.setCityRepo(cityRepo);
        spider.setPositionRepo(positionRepo);
        spider.setSalaryRepo(salaryRepo);
        spider.setWorkTypeRepo(workTypeRepo);
        List<? extends JobItem> data = spider.crawl();
        JobResponse response = new JobResponse();
        if (data == null) {
            response.setStatus(404);
            response.setTotal(0);
            response.setMessage("Failed to crawl data");
        }
        else {
            response.setStatus(200);
            response.setTotal(data.size());
            response.setMessage("Get data successfully");
            response.setData(data);
        }
        
        return response.getStatus() == 404 ? 
            new ResponseEntity<JobResponse>(response, HttpStatus.NOT_FOUND) : 
            new ResponseEntity<JobResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/add-data")
    public String doTask() {
        db = new DummyDatabase(cateRepo, cityRepo, positionRepo, salaryRepo, workTypeRepo);
        return "Added data";
    }

    @GetMapping("/crawl/123job")
    public ResponseEntity<JobResponse> crawl123Job(
        @RequestParam(required = false, name = "q") String keyword,
        @RequestParam(required = false, name = "l") String location,
        @RequestParam(required = false, name = "s") String salary,
        @RequestParam(required = false, name = "level") String workType,
        @RequestParam(required = false, name = "cate") String category
    ) {
        OneTwoThreeJobSpider spider = new OneTwoThreeJobSpider(keyword, category, "", 0, 5000000, location, 0, 0, 0);
        spider.setJobRepo(jobRepo);
        spider.setCateRepo(cateRepo);
        spider.setCityRepo(cityRepo);
        spider.setPositionRepo(positionRepo);
        spider.setSalaryRepo(salaryRepo);
        spider.setWorkTypeRepo(workTypeRepo);

        List<? extends JobItem> jobs = spider.crawl();
        
        JobResponse response = new JobResponse();
        if (jobs == null) {
            response.setStatus(500);
            response.setMessage("server error");
        } else if (jobs.size() == 0) {
            response.setStatus(404);
            response.setMessage("Not found any data");
        }
        else {
            response.setStatus(200);
            response.setMessage("Get data successfully");
            response.setTotal(jobs.size());
            response.setData(jobs);
        }

        return ResponseEntity.ok(response);
    }
}
