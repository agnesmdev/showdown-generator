package agnesm.dev.apis.impl;

import agnesm.dev.apis.PokemonApiClient;
import agnesm.dev.conf.ConfigProperties;
import agnesm.dev.models.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonApiClientTest {

    private final String host = "http://pokemon-api-test.fr";
    private final ConfigProperties properties = mock(ConfigProperties.class);
    private final PokemonApiClient pokemonApiClient = new PokemonApiClientImpl(properties);

    @BeforeEach
    public void before() {
        when(properties.getHost()).thenReturn(host);
    }

    @Test
    public void getPokemonByGenSucceeds() throws Exception {
        HttpRequest request = pokemonApiClient.getPokemonByGen(5);

        assertThat(request.uri().toString().equals(host + "/generation/5"));
        assertThat(request.headers().map().size() == 1);
        assertThat(!request.headers().allValues("Accept").isEmpty());
        assertThat(request.headers().allValues("Accept").get(0).equals("application/json"));
    }

    @Test
    public void getPokemonByIdSucceeds() throws Exception {
        HttpRequest request = pokemonApiClient.getPokemonById(453);

        assertThat(request.uri().toString().equals(host + "/pokemon/453"));
        assertThat(request.headers().map().size() == 1);
        assertThat(!request.headers().allValues("Accept").isEmpty());
        assertThat(request.headers().allValues("Accept").get(0).equals("application/json"));
    }

    @Test
    public void getMovesByTypeSucceeds() throws Exception {
        HttpRequest request = pokemonApiClient.getMovesByType(Type.ice.name());

        assertThat(request.uri().toString().equals(host + "/type/" + Type.ice.name()));
        assertThat(request.headers().map().size() == 1);
        assertThat(!request.headers().allValues("Accept").isEmpty());
        assertThat(request.headers().allValues("Accept").get(0).equals("application/json"));
    }
}
