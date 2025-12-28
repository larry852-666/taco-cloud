package org.example.tacocloud.controller.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.tacocloud.data.Ingredient;
import org.example.tacocloud.data.Ingredient.Type;
import org.example.tacocloud.data.Taco;
import org.example.tacocloud.data.TacoOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.example.tacocloud.data.repository.IngredientRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.tacocloud.data.Ingredient.Type.WRAP;
import static org.example.tacocloud.data.Ingredient.Type.PROTEIN;
import static org.example.tacocloud.data.Ingredient.Type.VEGGIES;
import static org.example.tacocloud.data.Ingredient.Type.SAUCE;
import static org.example.tacocloud.data.Ingredient.Type.CHEESE;

@Slf4j // относится к логам
@Controller // показывает что данный класс относится к контроллерам чтобы spring понимал что создавать
@RequestMapping("/design") // показывает путь в url который будет обрабатываться контроллерам
@SessionAttributes("tacoOrder") // говорит о том что надо будет работать с объектом TacoOrder в сессии
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = ingredientRepo.findAll();
//        List<Ingredient> ingredients = List.of(
//                new Ingredient("FLTO", "Flour Tortilla", WRAP),
//                new Ingredient("COTO", "Corn Tortilla", WRAP),
//                new Ingredient("GRBF", "Ground Beef", PROTEIN),
//                new Ingredient("CARN", "Carnitas", PROTEIN),
//                new Ingredient("TMTO", "Diced Tomatoes", VEGGIES),
//                new Ingredient("LETC", "Lettuce", VEGGIES),
//                new Ingredient("CHED", "Cheddar", CHEESE),
//                new Ingredient("JACK", "Monterrey Jack", CHEESE),
//                new Ingredient("SLSA", "Salsa", SAUCE),
//                new Ingredient("SRCR", "Sour Cream", SAUCE)
//        );

        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(
                    type.toString().toLowerCase(),
                    filterByType(ingredients, type)
            );
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder order) {
        if (errors.hasErrors()) {
            return "design";
        }

        order.addTaco(taco);
        log.info("Processing taco: {}", taco);
        return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
