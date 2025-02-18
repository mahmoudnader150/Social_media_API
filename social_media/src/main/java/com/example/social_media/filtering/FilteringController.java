package com.example.social_media.filtering;

import java.util.*;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.hibernate.sql.ast.tree.predicate.FilterPredicate;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public MappingJacksonValue filtering(){
        // 1- Init the class
        SomeBean someBean = new SomeBean("value1","value2","value3");
        // 2- Init mapping jackson and inject the class
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
        // 3- Init Property filter and specify the filters
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1");
        // 4- Give Id to the filter by passing it to Filter Provider (Like a list of filters)
        FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter",filter);
        // 5- passing filters to the setter of MappingJacksonValue
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    @GetMapping("/filtering-list")
    public MappingJacksonValue filteringList(){
        List<SomeBean> list = Arrays.asList(
                new SomeBean("value1","value2","value3"),
                new SomeBean("value4","value5","value6")
        );
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1");
        FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter",filter);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
}
