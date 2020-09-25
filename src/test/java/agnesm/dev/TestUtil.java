package agnesm.dev;

import agnesm.dev.models.GenerationInfo;
import agnesm.dev.models.Pokemon;
import agnesm.dev.models.Type;
import com.github.javafaker.Faker;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TestUtil {

    public Path resourceFilePath(String fileName) throws URISyntaxException {
        return Path.of(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI());
    }

    public GenerationInfo randomGenerationInfo() {
        Faker faker = new Faker();

        int id = faker.number().randomDigitNotZero();
        List<String> abilities = randomStringList(faker);
        List<Integer> pokemon = randomIntList(faker);
        List<String> moves = randomStringList(faker);

        return new GenerationInfo(id, abilities, pokemon, moves);
    }

    public Pokemon randomPokemon() {
        Faker faker = new Faker();

        int number = (int) faker.number().randomNumber();
        String name = faker.letterify("");
        Type primaryType = randomType(faker);
        Type secondaryType = (faker.bool().bool()) ? randomType(faker) : null;
        return new Pokemon(number, name, primaryType, secondaryType);
    }

    public List<Pokemon> randomPokemonTeam(boolean moves) {
        return IntStream.rangeClosed(1, 6).boxed().map(i -> {
            Pokemon pokemon = randomPokemon();
            if (moves) pokemon.setMoves(randomMoves());
            pokemon.setAbility(randomAbility());

            return pokemon;
        }).collect(Collectors.toList());
    }

    public List<String> randomMoves() {
        return randomStringList(new Faker());
    }

    private Type randomType(Faker faker) {
        return Type.values()[(int) faker.number().randomDouble(0, 0, Type.values().length)];
    }

    private List<Integer> randomIntList(Faker faker) {
        return IntStream.rangeClosed(0, (int) faker.number().randomNumber(2, true)).boxed().collect(Collectors.toList());
    }

    private List<String> randomStringList(Faker faker) {
        return randomIntList(faker).stream().map(i -> faker.letterify("??????????")).collect(Collectors.toList());
    }

    private String randomAbility() {
        return new Faker().letterify("??????????");
    }
}
