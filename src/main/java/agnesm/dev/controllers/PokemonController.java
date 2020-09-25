package agnesm.dev.controllers;

import agnesm.dev.models.Pokemon;
import agnesm.dev.services.PokemonService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class PokemonController {

    @Autowired
    private Logger logger;

    @Autowired
    private PokemonService service;

    @GetMapping("/randomizer")
    CompletableFuture<String> randomizer(@RequestParam int gen, @RequestParam boolean moves) {
        logger.info("Request to generated randomized team for gen " + gen + " and " + ((moves) ? "" : "no ") + "moves");
        return service.generateRandomizedTeam(gen, moves).thenApply(pokemon -> {
            logger.info("Successfully generated randomized team for gen " + gen + " and " + ((moves) ? "" : "no ") + "moves");
            return pokemon.stream().map(Pokemon::toShowdownFormat).collect(Collectors.joining("\n"));
        });
    }
}