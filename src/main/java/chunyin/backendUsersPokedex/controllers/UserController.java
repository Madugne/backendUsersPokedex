package chunyin.backendUsersPokedex.controllers;

import chunyin.backendUsersPokedex.entities.User;
import chunyin.backendUsersPokedex.services.PokemonService;
import chunyin.backendUsersPokedex.services.UserService;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService usersService;

    @Autowired
    PokemonService pokemonService;

    // GET http://localhost:3001/users
    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String sort) {
        return usersService.getUser(page, size, sort);
    }

    // GET http://localhost:3001/users/{id}

    @GetMapping("/{userId}")
    public User findById(@PathVariable UUID userId) {
        return usersService.findById(userId);
    }

    // PUT http://localhost:3001/users/{id} (+ req.body)

    @PutMapping("/{userId}")
    public User findAndUpdate(@PathVariable UUID utentiId, @RequestBody User body){
        return usersService.findByIdAndUpdate(utentiId, body);
    }

    // DELETE http://localhost:3001/users/{id}

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> findByIdAndDelete(@PathVariable UUID userId) {

        usersService.findByIDAndDelete(userId);
        return ResponseEntity.ok("User deleted");
    }

    // POST http://localhost:3001/users/{id}/avatar (+ file)

    @PostMapping("/{userId}/avatar")
    public User updateAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable UUID utentiId) {
        try {
            return usersService.uploadAvatar(utentiId, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //aggiunta pokemon
    @PostMapping("/{userId}/pokemon/{pokemonId}")
    public ResponseEntity<String> addUserPokemon(@PathVariable UUID userId, @PathVariable int pokemonId) {
        usersService.addUserPokemon(userId, pokemonId);
        return ResponseEntity.ok("Pokemon added successfully");
    }

    //rimozione pokemon
    @DeleteMapping("/{userId}/pokemon/{pokemonId}")
    public ResponseEntity<String> removeUserPokemon(@PathVariable UUID userId, @PathVariable int pokemonId) {
        usersService.removeUserPokemon(userId, pokemonId);
        return ResponseEntity.ok("Pokemon deleted successfully");
    }

    // Aggiungiamo il metodo per ottenere la lista dei Pokémon dell'utente
    @GetMapping("/{userId}/pokemon")
    public Set<Integer> getUserPokemonList(@PathVariable UUID userId) {
        return usersService.getUserPokemonList(userId);
    }

//    @GetMapping("/pokemon/{pokemonId}")
//    public JSONObject getPokemonDetails(@PathVariable int pokemonId) {
//        return pokemonService.getPokemonDetails(pokemonId);
//    }

    @GetMapping("/pokemon/{pokemonId}")
    public ResponseEntity<String> getPokemonDetails(@PathVariable int pokemonId) {
        String pokemonDetails = pokemonService.getPokemonDetails(pokemonId);
        return ResponseEntity.ok(pokemonDetails);
    }
}
