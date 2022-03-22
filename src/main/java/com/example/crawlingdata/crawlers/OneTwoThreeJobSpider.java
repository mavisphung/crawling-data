package com.example.crawlingdata.crawlers;

import java.util.ArrayList;
import java.util.List;

import com.example.crawlingdata.repositories.CategoryRepository;
import com.example.crawlingdata.repositories.CityRepository;
import com.example.crawlingdata.repositories.CrawlHistoryRepository;
import com.example.crawlingdata.repositories.JobRepository;
import com.example.crawlingdata.repositories.KeywordRepository;
import com.example.crawlingdata.repositories.PositionRepository;
import com.example.crawlingdata.repositories.SalaryRepository;
import com.example.crawlingdata.repositories.WorkTypeRepository;
import com.example.crawlingdata.responses.models.CrawlHistory;
import com.example.crawlingdata.responses.models.JobItem;
import com.example.crawlingdata.responses.models.Keyword;
import com.example.crawlingdata.util.OneTwoThreeJobData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector.SelectorParseException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OneTwoThreeJobSpider extends Crawler {
    //   https://123job.vn/tuyen-dung?exp=&level=&s=&jt=&cat=&q=java&l=&label=
    private JobRepository jobRepo;
    private CategoryRepository cateRepo;
    private CityRepository cityRepo;
    private PositionRepository positionRepo;
    private SalaryRepository salaryRepo;
    private WorkTypeRepository workTypeRepo;
    private KeywordRepository kwRepo;
    private CrawlHistoryRepository historyRepo;

    public OneTwoThreeJobSpider(String keyword, String jobCategory, String companyField, int minSalary, int maxSalary, String location, int minimumExperience, int position, int workType) {
        super("https://123job.vn/", keyword, jobCategory, companyField, minSalary, maxSalary, location, minimumExperience, position, workType);
    }

    @Override
    public String formatUrl(String keyword, String location, String workType, String category, String companyField,
            String position, double fromSalary, double toSalary, int pageNum) {
        
        StringBuilder sb = new StringBuilder("/tuyen-dung?");
        if (keyword != null && !keyword.isBlank()) {
            sb.append("q=" + keyword + "&");
        } else {
            sb.append("q=&");
        }

        if (location != null && !location.isBlank()) {
            sb.append("l=" + location + "&");
        }

        return sb.toString();
    }

    @Override
    public List<JobItem> crawl() {
        CrawlHistory history = new CrawlHistory(this.getClass().getSimpleName(), super.getKeyword(), super.getLocation());
        historyRepo.save(history);
        String url = super.getBaseUrl() + formatUrl(super.getKeyword(), super.getLocation(), super.getWorkType() + "", super.getJobCategory(), super.getCompanyField(), super.getPosition() + "", getMinSalary(), getMaxSalary(), 1);
        System.out.println("Url: " + url);
        kwRepo.save(new Keyword(super.getKeyword()));
        List<JobItem> resultsList = new ArrayList<>();
        Document docs = null;
        Elements foundJobs = null;
        try {
            var currentPage = 1;
            String tempUrl = url + "page=";
            docs = Jsoup.connect(tempUrl + currentPage).get();
            List<String> urls = new ArrayList<>();
            for (int i = 1; i <= OneTwoThreeJobData.TOTAL_PAGE; i++) {
                urls.add(tempUrl + i);
            }
            List<Elements> docsList = new ArrayList<>();
            for (String myUrl : urls) {
                docs = Jsoup.connect(myUrl).get();
                foundJobs = docs.select(OneTwoThreeJobData.JOB_ITEMS);
                if (foundJobs.isEmpty())
                    return null;
                docsList.add(foundJobs);
            }
            
            for (Elements jobList : docsList) {
                for (Element job : jobList) {
                    String logo = job.selectFirst(OneTwoThreeJobData.LOGO).attr("data-src");
                    String jobName = job.selectFirst(OneTwoThreeJobData.JOB_NAME).text();
                    String companyName = job.selectFirst(OneTwoThreeJobData.COMPANY).text();
                    String location = job.selectFirst(OneTwoThreeJobData.LOCATION).text();
                    var salaryEl = job.selectFirst(OneTwoThreeJobData.SALARY);
                    String salary = "";
                    if (salaryEl == null) {
                        salary = "Thỏa thuận";
                    } else {
                        salary = salaryEl.text();
                    }
                    
                    var tempo = new JobItem(jobName, companyName, logo, location, salary);
                    tempo.setHistory(history);
                    // jobsList.add(new JobItem(jobName, companyName, logo, location, salary));
                    resultsList.add(tempo);
                }
            }

            // foundJobs = docs.select(OneTwoThreeJobData.JOB_ITEMS);
            // int jobsTotal = 0;
            // System.out.println("Total jobs: " + jobsTotal);
            // while (currentPage <= OneTwoThreeJobData.TOTAL_PAGE) {
            //     System.out.println("Fetching from url: " + tempUrl + currentPage);
            //     for (Element job : foundJobs) {
            //         String logo = job.selectFirst(OneTwoThreeJobData.LOGO).attr("data-src");
            //         String jobName = job.selectFirst(OneTwoThreeJobData.JOB_NAME).text();
            //         String companyName = job.selectFirst(OneTwoThreeJobData.COMPANY).text();
            //         String location = job.selectFirst(OneTwoThreeJobData.LOCATION).text();
            //         var salaryEl = job.selectFirst(OneTwoThreeJobData.SALARY);
            //         String salary = "";
            //         if (salaryEl == null) {
            //             salary = "Thỏa thuận";
            //         } else {
            //             salary = salaryEl.text();
            //         }

            //         // jobsList.add(new JobItem(jobName, companyName, logo, location, salary));
            //         jobsList.add(new JobItem(jobName, companyName, logo, location, salary));
            //     }
            //     currentPage += 1;
            //     if (currentPage > OneTwoThreeJobData.TOTAL_PAGE)
            //         break;
            //     docs = Jsoup.connect(tempUrl + currentPage).get();
            //     foundJobs = docs.select(OneTwoThreeJobData.JOB_ITEMS);
            // }
            
            System.out.println("Total jobs in list: " + resultsList.size());
            return jobRepo.saveAll(resultsList);
            // return resultsList;
        } catch (SelectorParseException spe) {
            return new ArrayList<JobItem>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
