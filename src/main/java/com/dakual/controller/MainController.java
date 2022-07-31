package com.dakual.controller;

import com.dakual.model.Product;
import com.dakual.service.ProductService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    ProductService productService;

    public MainController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexPage(Model model) {
        model.addAttribute("productObject", new Product());
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String newProduct(@ModelAttribute("productObject") Product product, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        String message = "Product successfully saved";
        if (file.isEmpty()) {
            message = "Product not saved!";
            return "redirect:/";
        }

        try {
            byte[] fileContent   = file.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String ctreatedAt = dateFormat.format(date);

            product.setImage("data:img/png;base64," + encodedString);
            product.setCreatedAt(ctreatedAt);
            productService.save(product);
        } catch (IOException e) {
            message = "Product not saved!";
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex, RedirectAttributes redirectAttributes) {
        String name    = ex.getParameterName();
        String message = "Parameter '" + name + "' is missing";

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/";
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String productPage(@RequestParam(value = "id", required = true) String id, Model model) {
        Optional<Product> product = productService.findbyId(id);
        List<Product> products    = productService.findAll();

        model.addAttribute("productObject", new Product());
        model.addAttribute("product", product.get());
        model.addAttribute("products", products);

        return "product";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String productRemove(@RequestParam(value = "id", required = true) String id, Model model, RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Product removed!");

        return "redirect:/";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String productSearch(@RequestParam(value = "q", required = true) String query, Model model) {
        List<Product> products    = productService.findByName(query);
        model.addAttribute("productObject", new Product());
        model.addAttribute("products", products);

        return "search";
    }
}
