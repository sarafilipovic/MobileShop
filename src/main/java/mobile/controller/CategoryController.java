package mobile.controller;

import mobile.repositories.CategoryRepository;
import mobile.model.Category;
import mobile.model.UserDetails;
import mobile.model.Kosarica;

import jakarta.validation.Valid;
import mobile.repositories.KosaricaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;


@Controller
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    KosaricaRepository kosaricaRepository;









    @GetMapping("/category")
    public String showCategories (Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId();
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Kategorije");

        List<Kosarica> kosaricas = kosaricaRepository.findByCreatedBy(userDetails.getUser());

        int kosaraBroj = kosaricas.size();

        if (kosaraBroj > 0) {
            model.addAttribute("kosarice", kosaricas);
            model.addAttribute("kosaraBroj", kosaraBroj);
            model.addAttribute("prikazi", true);
        } else {
            model.addAttribute("prikazi", false);
        }

        return "category";
    }

    @PostMapping("/category/add")
    public String dodajKategoriju (@Valid Category category, BindingResult result, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("added", true);
            model.addAttribute("activeLink", "Kategorije");
            return "category";
        }
        categoryRepository.save(category);


        return "redirect:/category";
    }

    @GetMapping("/category/delete/{id}")
    public String izbrisiKategoriju (@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

//

        try {
            categoryRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("greskaCategory", "Imate mobitel s ovom kategorijom");
        }
        return "redirect:/category";
    }
}
