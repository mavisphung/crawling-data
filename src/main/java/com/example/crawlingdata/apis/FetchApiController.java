package com.example.crawlingdata.apis;

import java.io.IOException;
import java.util.ArrayList;

import com.example.crawlingdata.models.WikiItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class FetchApiController {
    

    @GetMapping(value = {"", "/"})
    public String fetch() {
        return "Hello world";
    }

    @GetMapping("/wiki/")
    public ResponseEntity fetchDataFromWiki() {
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
}
