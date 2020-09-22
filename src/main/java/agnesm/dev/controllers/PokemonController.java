package agnesm.dev.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import agnesm.dev.models.Pokemon;
import agnesm.dev.services.PokemonService;
import com.google.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PokemonController {

    @Autowired
    private PokemonService service;

    @GetMapping("/randomizer")
    CompletableFuture<List<Pokemon>> randomized(@RequestParam int gen, @RequestParam boolean moves) {
        return service.generateRandomizedTeam(gen, moves);
    }
}