package com.example.customer_management.controller;

import com.example.customer_management.entity.City;
import com.example.customer_management.entity.Country;
import com.example.customer_management.repository.CityRepository;
import com.example.customer_management.repository.CountryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master")
@CrossOrigin("*")
public class MasterController {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public MasterController(CityRepository cityRepository,
            CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    // GET CITIES
    @GetMapping("/cities")
    public List<City> getCities() {
        return cityRepository.findAll();
    }

    // GET COUNTRIES
    @GetMapping("/countries")
    public List<Country> getCountries() {
        return countryRepository.findAll();
    }
}