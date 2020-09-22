# Showdown Generator

This API allows to create randomized Pokémon teams to compete on [Pokémon Showdown](https://play.pokemonshowdown.com/).

## Launch
```
mvn clean spring-boot:run
```

## /randomizer

### Parameters

- `gen` : from 1 to 8
- `moves` : boolean to indicate if the moveset is wanted

### Description

This endpoint will give you a formatted text required by Showdown to create a team.

All Pokémon will have randomized abilities and, if specified, randomized moves preferably according to their types. 