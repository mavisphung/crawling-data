package com.example.crawlingdata.apis;

import java.io.IOException;
import java.util.ArrayList;

import com.example.crawlingdata.responses.JobResponse;
import com.example.crawlingdata.responses.models.JobItem;
import com.example.crawlingdata.responses.models.WikiItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class FetchApiController {
    
    private WebDriver web;

    public FetchApiController(WebDriver web) {
        this.web = web;
    }

    @GetMapping(value = {"", "/"})
    public String fetch() {
        String url = "https://www.topcv.vn";
        web.get(url);
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
    public ResponseEntity<Object> findJob(@RequestParam String url, @RequestParam String jobName) {
        System.out.println("findJob invoked");
        var jobs = crawlJobsFromTopCv(url, jobName);
        JobResponse response = new JobResponse();
        response.setStatus(200);
        response.setMessage("Get successfully");
        response.setData(jobs);
        return ResponseEntity.ok(response);
    }

    private ArrayList<JobItem> crawlJobsFromTopCv(String url, String jobName) {
        var fullUrl = getUrl(url);
        Document doc = null;
        try {
            doc = Jsoup.connect(fullUrl + jobName).get();
        } catch (IOException e) {
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
            var deadline = item.select(".body .content .deadline").first().text();
            var company = item.select(".body .company a").first().text();

            JobItem job = new JobItem(title, company, logo, deadline);
            jobs.add(job);
            // System.out.println(img);
        }
        return jobs;
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
