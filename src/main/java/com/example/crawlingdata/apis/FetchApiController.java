package com.example.crawlingdata.apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.crawlingdata.crawlers.TopCvSpider;
import com.example.crawlingdata.repositories.CategoryRepository;
import com.example.crawlingdata.repositories.CityRepository;
import com.example.crawlingdata.repositories.JobRepository;
import com.example.crawlingdata.responses.JobResponse;
import com.example.crawlingdata.responses.models.JobItem;
import com.example.crawlingdata.responses.models.WikiItem;
import com.example.crawlingdata.util.DummyDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.http.HttpStatus;
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
    private WebDriver web;
    private final JobRepository jobRepo;
    private final CategoryRepository cateRepo;
    private final CityRepository cityRepo;
    private DummyDatabase db;

    public FetchApiController(WebDriver web, JobRepository jobRepo, CategoryRepository cateRepo, CityRepository cityRepo) {
        this.web = web;
        jobList = new ArrayList<JobItem>();
        this.jobRepo = jobRepo;
        this.cateRepo = cateRepo;
        this.cityRepo = cityRepo;
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
        db = new DummyDatabase(cateRepo, cityRepo);
        return "Added data";
    }
}
