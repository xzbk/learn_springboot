package com.group.controller;

import com.group.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloThymeLeaf {
    @RequestMapping("helloThymeleaf")
    public String helloThymeleaf(Model model){
        model.addAttribute("welcome","welcomeThymeLeaf");
        return "tl-hello";
    }

    @RequestMapping("list")
    public String list(Model model){
        List<Product> products = new ArrayList<Product>();
        products.add(new Product("name1",22,354));
        products.add(new Product("name2",25,239));
        products.add(new Product("name3",62,349));
        model.addAttribute("prods",products);
        return "tl-list";
    }
}
