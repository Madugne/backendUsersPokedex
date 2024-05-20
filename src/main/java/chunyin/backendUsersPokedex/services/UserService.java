package chunyin.backendUsersPokedex.services;

import chunyin.backendUsersPokedex.dao.UserDAO;
import chunyin.backendUsersPokedex.entities.User;
import chunyin.backendUsersPokedex.exceptions.BadRequestException;
import chunyin.backendUsersPokedex.exceptions.NotFoundException;
import chunyin.backendUsersPokedex.payloads.NewUserDTO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private Cloudinary cloudinaryUploader;

//    @Autowired
//    private PokemonService pokemonService;

    @Autowired
    RestTemplate restTemplate;
    private final String pokeApiBaseUrl = "https://pokeapi.co/api/v2";

    public User save(NewUserDTO body) throws IOException {
        userDAO.findByEmail(body.email()).ifPresent(
                utente -> {
                    throw  new BadRequestException("The email " + body.email() + " is already taken");
                }
        );
        User utente = new User(body.username(), bcrypt.encode(body.password()), body.email(), body.name(), body.surname(), "https://ui-avatars.com/api/?name="+ body.name().charAt(0) + "+" + body.surname().charAt(0));
        return userDAO.save(utente);
    }

    public Page<User> getUser(int page, int size, String sort){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userDAO.findAll(pageable);
    }

    public User findById(UUID id){
        return userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByIdAndUpdate(UUID id, User body){
        User found = this.findById(id);
        found.setName(body.getName());
        found.setSurname(body.getSurname());
        found.setEmail(body.getEmail());
        found.setPassword(body.getPassword());
        found.setUsername(body.getUsername());
        found.setAvatarUrl("https://ui-avatars.com/api/?name=" + body.getName().charAt(0) + "+" + body.getSurname().charAt(0));
        return userDAO.save(found);
    }

    public void findByIDAndDelete(UUID id) {
        User found = this.findById(id);
        userDAO.delete(found);
    }

    public User uploadAvatar(UUID id, MultipartFile file) throws IOException{
        User found = this.findById(id);
        String avatarUrl = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatarUrl(avatarUrl);
        return userDAO.save(found);
    }

    public User findByEmail(String email){
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));
    }

    // Aggiungi metodi per gestire i Pokémon

//    public void addUserPokemon(UUID userId, int pokemonId) {
//        User user = this.findById(userId);
//        Pokemon pokemon = pokemonService.getPokemonById(pokemonId);
//        user.getPokemonList().add(pokemon);
//        userDAO.save(user);
//    }
//
//    public List<Pokemon> getUserPokemonList(UUID userId) {
//        User user = this.findById(userId);
//        return new ArrayList<>(user.getPokemonList());
//    }

    // Metodo per aggiungere un Pokemon all'utente
    public void addUserPokemon(UUID userId, int pokemonId) {
        User user = findById(userId);

        // Effettua una richiesta all'API PokeAPI per ottenere i dettagli del Pokemon
        String pokemonUrl = pokeApiBaseUrl + "/pokemon/" + pokemonId;
        restTemplate.getForObject(pokemonUrl, Object.class);

        // Aggiungi il Pokemon alla lista dei Pokemon dell'utente
        user.getPokemonList().add(pokemonId);

        // Aggiorna l'utente nel database
        userDAO.save(user);
    }

    // Metodo per rimuovere un Pokemon all'utente
    public void removeUserPokemon(UUID userId, int pokemonId) {
        User user = findById(userId);

        // Effettua una richiesta all'API PokeAPI per ottenere i dettagli del Pokemon
        String pokemonUrl = pokeApiBaseUrl + "/pokemon/" + pokemonId;
        restTemplate.getForObject(pokemonUrl, Object.class);

        // Aggiungi il Pokemon alla lista dei Pokemon dell'utente
        user.getPokemonList().remove(pokemonId);

        // Aggiorna l'utente nel database
        userDAO.save(user);
    }



    /// Metodo per ottenere la lista dei Pokemon dell'utente
    public Set<Integer> getUserPokemonList(UUID userId) {
        User user = findById(userId);
        return user.getPokemonList();
    }

}
