package agnesm.dev.apis.impl;

import agnesm.dev.TestUtil;
import agnesm.dev.apis.PokemonApi;
import agnesm.dev.apis.PokemonApiClient;
import agnesm.dev.models.GenerationInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PokemonApiTest extends TestUtil {

    private final Logger logger = LoggerFactory.getLogger(PokemonApiTest.class);
    private final HttpClient httpClient = mock(HttpClient.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PokemonApiClient client = mock(PokemonApiClient.class);
    private final HttpRequest request = mock(HttpRequest.class);
    private final HttpResponse<String> response = mock(HttpResponse.class);

    private final PokemonApi pokemonApi = new PokemonApiImpl(logger, httpClient, objectMapper, client);

    @BeforeEach
    private void before() {
        reset(httpClient, client, request, response);
    }

    @Test
    public void getGenerationInfoSucceeds() throws Exception {
        String infoResponse = Files.readString(resourceFilePath("json/generation_info_response.json"));
        when(client.getPokemonByGen(anyInt())).thenReturn(request);
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(
                CompletableFuture.completedFuture(response)
        );
        when(response.statusCode()).thenReturn(HttpStatus.OK.value());
        when(response.body()).thenReturn(infoResponse);

        CompletableFuture<GenerationInfo> result = pokemonApi.getGenerationInfo(3);
        Thread.sleep(100);

        assertThat(result.isDone());
        GenerationInfo info = result.get();

        assertThat(info.getId() == 3);
        assertThat(info.getAbilities().isEmpty());
        assertThat(info.getMoves().isEmpty());

        verify(client).getPokemonByGen(3);
        verify(httpClient).sendAsync(request, HttpResponse.BodyHandlers.ofString());
        verify(response).statusCode();
        verify(response).body();
    }

    @Test
    public void getGenerationInfoIncorrectBody() throws Exception {
        String incorrectBody = "{\"id\": 2}";
        when(client.getPokemonByGen(anyInt())).thenReturn(request);
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(
                CompletableFuture.completedFuture(response)
        );
        when(response.statusCode()).thenReturn(HttpStatus.OK.value());
        when(response.body()).thenReturn(incorrectBody);

        CompletableFuture<GenerationInfo> result = pokemonApi.getGenerationInfo(3);
        Thread.sleep(100);
        assertThat(result.isCompletedExceptionally());

        try {
            result.join();
        } catch (Exception ex) {
            verify(client).getPokemonByGen(3);
            verify(httpClient).sendAsync(request, HttpResponse.BodyHandlers.ofString());
            verify(response).statusCode();
            verify(response).body();
        }
    }
}
