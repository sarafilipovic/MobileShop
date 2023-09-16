package mobile.controller;
import mobile.model.*;
import mobile.repositories.MobileRepository;
import mobile.repositories.UserRepository;
import mobile.model.*;
import mobile.repositories.CategoryRepository;
import jakarta.validation.Valid;
import mobile.repositories.KosaricaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class MobileController {
    @Autowired
    MobileRepository mobileRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    KosaricaRepository kosaricaRepository;







    @GetMapping("/mobile")
    public String prikaziMobitele(Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId(); // ili koristite metodu kojom dobavljate ID korisnika
        List<Category> categories = categoryRepository.findAll();
        System.out.println(categories.size());
        model.addAttribute("mobiles", mobileRepository.findAll());
        model.addAttribute("categories", categories);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("mobile", new Mobile());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Igre");
        User userr = userDetails.getUser();
        Long userIdd = user.getUserId();


        List<Kosarica> kosaricas = kosaricaRepository.findByCreatedBy(userDetails.getUser());

        int kosaraBroj = kosaricas.size();

        if (kosaraBroj > 0) {
            model.addAttribute("watches", kosaricas);
            model.addAttribute("kosaraBroj", kosaraBroj);
            model.addAttribute("prikazi", true);
        } else {
            model.addAttribute("prikazi", false);
        }

        return "mobile";
    }


    @GetMapping("/dodajMobitel")
    public String getMobiles(Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId(); // ili koristite metodu kojom dobavljate ID korisnika
        List<Category> categories = categoryRepository.findAll();
        System.out.println(categories.size());
        model.addAttribute("mobiles", mobileRepository.findAll());
        model.addAttribute("categories", categories);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("mobile", new Mobile());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Igre");
        User userr = userDetails.getUser();

        Long userIdd = user.getUserId();


        List<Kosarica> kosaricas = kosaricaRepository.findByCreatedBy(userDetails.getUser());

        int kosaraBroj = kosaricas.size();

        if (kosaraBroj > 0) {
            model.addAttribute("watches", kosaricas);
            model.addAttribute("kosaraBroj", kosaraBroj);
            model.addAttribute("prikazi", true);
        } else {
            model.addAttribute("prikazi", false);
        }

        return "dodajMobitel";
    }
    @PostMapping("/mobile/add")
    public String addMovie(@Valid Mobile mobile, BindingResult result, Model model, RedirectAttributes redirectAttributes, UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        if (result.hasErrors()) {
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("mobile", mobile);
            model.addAttribute("mobiles", mobileRepository.findAll());
            model.addAttribute("added", true);
            model.addAttribute("activeLink", "Igre");
            return "mobile";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);
        mobile.setUser(selectedUser);
        Long categoryId = mobile.getCategory().getId();
        Category selectedCategory = categoryRepository.findById(categoryId).orElse(null);
        mobile.setCategory(selectedCategory);

        mobileRepository.save(mobile);

        return "redirect:/mobile";
    }



    @GetMapping("/mobile/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        model.addAttribute("user", user);
        Mobile mobile = mobileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        model.addAttribute("mobile", mobile);
        model.addAttribute("mobiles", mobileRepository.findAll());
        model.addAttribute("activeLink", "Kategorije");
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        User userr = userDetails.getUser();
        System.out.println("User je" + userr);
        Long userIdd = user.getUserId();
        List<Kosarica> kosaricas = kosaricaRepository.findByCreatedBy(userDetails.getUser());

        int kosaraBroj = kosaricas.size();

        if (kosaraBroj > 0) {
            model.addAttribute("watches", kosaricas);
            model.addAttribute("watchCount", kosaraBroj);
            model.addAttribute("prikazi", true);
        } else {
            model.addAttribute("prikazi", false);
        }
        return "mobile_edit";
    }

    @PostMapping("mobile/edit/{id}")
    public String editMovie (@PathVariable("id") Long id, @Valid Mobile mobile, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        if (result.hasErrors()) {
            model.addAttribute("mobile", mobile);
            model.addAttribute("activeLink", "Igre");
            return "mobile_edit";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);
        mobile.setUser(selectedUser);
        mobileRepository.save(mobile);

        return "redirect:/mobile";
    }


    @GetMapping("/mobile/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        Mobile mobile = mobileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pogrešan ID"));
        try {
            mobileRepository.delete(mobile);

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Ovaj mobitel je dodan u kosaricu. Izbrisite mobitel iz vase kosarice kako bi mogli izvesti akciju brisanja ovog modela mobitela");
        }
        return "redirect:/mobile";
    }

}
