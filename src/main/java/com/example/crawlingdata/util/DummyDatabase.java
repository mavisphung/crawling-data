package com.example.crawlingdata.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;

import com.example.crawlingdata.repositories.CategoryRepository;
import com.example.crawlingdata.repositories.CityRepository;
import com.example.crawlingdata.repositories.JobRepository;
import com.example.crawlingdata.responses.models.Category;
import com.example.crawlingdata.responses.models.City;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class DummyDatabase {

    private static String[] paths = { "categories.json", "cities.json" };

    private CategoryRepository cateRepo;
    private CityRepository cityRepo;
    
    public DummyDatabase(CategoryRepository cateRepo, CityRepository cityRepo) {
        this.cateRepo = cateRepo;
        this.cityRepo = cityRepo;
        initalizeCategories();
        intializeCities();
        log.info("Intialized database successfully");
        // System.out.println("Intialized database successfully");
    }


    private void intializeCities() {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(paths[1]), "UTF-8"));
            String line = br.readLine();
            while (line != null) {
                // System.out.println(line);
                sb.append(line);
                line = br.readLine();
                // index += 1;
            }
            // System.out.println(sb.toString());
            // Convert to java object
            Gson gson = new Gson();
            List<City> cities = gson.fromJson(sb.toString(), new TypeToken<List<City>>(){}.getType());
            cityRepo.saveAllAndFlush(cities);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private void initalizeCategories() {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(paths[0]), "UTF-8"));
            String line = br.readLine();
            while (line != null) {
                // System.out.println(line);
                sb.append(line);
                line = br.readLine();
                // index += 1;
            }
            // System.out.println(sb.toString());
            // Convert to java object
            Gson gson = new Gson();
            List<Category> categories = gson.fromJson(sb.toString(), new TypeToken<List<Category>>(){}.getType());
            cateRepo.saveAllAndFlush(categories);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
