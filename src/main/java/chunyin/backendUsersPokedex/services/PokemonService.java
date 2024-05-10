package chunyin.backendUsersPokedex.services;

import kong.unirest.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokemonService {

    private final RestTemplate restTemplate;
    private final String pokeApiBaseUrl = "https://pokeapi.co/api/v2";

    public PokemonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Metodo per ottenere i dettagli di un Pokémon dall'API PokeAPI
//    public JSONObject getPokemonDetails(int pokemonId) {
//        String pokemonUrl = pokeApiBaseUrl + "/pokemon/" + pokemonId;
//        return restTemplate.getForObject(pokemonUrl, JSONObject.class);
//    }
    public String getPokemonDetails(int pokemonId) {
        String pokemonUrl = pokeApiBaseUrl + "/pokemon/" + pokemonId;
        ResponseEntity<String> response = restTemplate.getForEntity(pokemonUrl, String.class);
        return response.getBody();
    }
}
