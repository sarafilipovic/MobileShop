package mobile.controller;


import mobile.model.Kosarica;
import mobile.model.Mobile;
import mobile.model.User;
import mobile.model.UserDetails;
import mobile.repositories.CategoryRepository;
import mobile.repositories.MobileRepository;
import mobile.repositories.UserRepository;
import mobile.model.*;
import mobile.repositories.KosaricaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class KosaricaController {
    @Autowired
    MobileRepository mobileRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    KosaricaRepository kosaricaRepository;


    @GetMapping("/kosarica")
    public String showWatch(Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId(); // ili koristite metodu kojom dobavljate ID korisnika
        List<Mobile> mobiles = mobileRepository.findAll();

        model.addAttribute("kosarice", kosaricaRepository.findAll());
        model.addAttribute("mobiles", mobiles);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("kosarica", new Kosarica());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Igre");
        User userr = userDetails.getUser();
        Long userIdd = user.getUserId();
        List<Kosarica> kosarice= kosaricaRepository.findByCreatedBy(userDetails.getUser());

        int kosaraBroj = kosarice.size();

        if (kosaraBroj > 0) {
            model.addAttribute("kosarice", kosarice);
            model.addAttribute("kosaraBroj", kosaraBroj);
            model.addAttribute("prikazi", true);
        } else {
            model.addAttribute("prikazi", false);
        }

        return "kosarica";
    }



    @GetMapping("/kosarica/{id}")
    public String addWatch(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes, UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();



        Mobile selectedMobile = mobileRepository.findById(id).orElse(null);

        if (selectedMobile == null) {
            return "redirect:/error";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);

        Kosarica existingKosarica = kosaricaRepository.findByCreatedByAndMobile(selectedUser, selectedMobile);

        if (existingKosarica != null) {
            redirectAttributes.addFlashAttribute("error", "Mobitel postoji u vasoj kosarici");
            return "redirect:/mobile";
        }

        Kosarica newKosarica = new Kosarica();
        newKosarica.setUser(selectedUser);
        newKosarica.setMobile(selectedMobile);
        newKosarica.setUser(selectedUser);

        kosaricaRepository.save(newKosarica);


        return "redirect:/mobile";
    }

    @GetMapping("/kosarica/delete/{id}")
    public String deleteWatch(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

       Kosarica kosarica = kosaricaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pogrešan ID"));
        kosaricaRepository.delete(kosarica);
        redirectAttributes.addFlashAttribute("successDeleteWatch", "Uspjesno izbrisano!");


        return "redirect:/kosarica";
    }

}
