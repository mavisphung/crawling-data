package com.example.crawlingdata.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.example.crawlingdata.repositories.CategoryRepository;
import com.example.crawlingdata.repositories.CityRepository;
import com.example.crawlingdata.repositories.PositionRepository;
import com.example.crawlingdata.repositories.SalaryRepository;
import com.example.crawlingdata.repositories.WorkTypeRepository;
import com.example.crawlingdata.responses.models.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class DummyDatabase {

    private static String[] paths = { "categories.json", "cities.json", "position.json", "salary.json", "workTypes.json" };

    private CategoryRepository cateRepo;
    private CityRepository cityRepo;
    private PositionRepository positionRepo;
    private SalaryRepository salaryRepo;
    private WorkTypeRepository workTypeRepo;
    
    public DummyDatabase(
        CategoryRepository cateRepo, 
        CityRepository cityRepo, 
        PositionRepository positionRepo, 
        SalaryRepository salaryRepo, 
        WorkTypeRepository workTypeRepo
    ) {
        this.cateRepo = cateRepo;
        this.cityRepo = cityRepo;
        this.positionRepo = positionRepo;
        this.salaryRepo = salaryRepo;
        this.workTypeRepo = workTypeRepo;
        initalizeCategories();
        intializeCities();
        intializeSalaries();
        intializePositions();
        intializeWorkTypes();
        log.info("Intialized database successfully");
        // System.out.println("Intialized database successfully");
    }

    private void intializeWorkTypes() {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(paths[4]), "UTF-8"));
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
            List<WorkType> workTypes = gson.fromJson(sb.toString(), new TypeToken<List<WorkType>>(){}.getType());
            workTypeRepo.saveAllAndFlush(workTypes);
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

    private void intializePositions() {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(paths[2]), "UTF-8"));
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
            List<Position> positions = gson.fromJson(sb.toString(), new TypeToken<List<Position>>(){}.getType());
            positionRepo.saveAllAndFlush(positions);
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

    private void intializeSalaries() {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(paths[3]), "UTF-8"));
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
            List<Salary> salaries = gson.fromJson(sb.toString(), new TypeToken<List<Salary>>(){}.getType());
            salaryRepo.saveAllAndFlush(salaries);
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
