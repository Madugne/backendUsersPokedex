package chunyin.backendUsersPokedex.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"password", "authorities", "accountNonExpired", "credentialsNonExpired", "accountNonLocked", "enabled"})
public class User implements UserDetails {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    @Column(name = "avatar")
    private String avatarUrl;

    // Aggiungi questa relazione per gestire la lista di Pokemon dell'utente
    @ElementCollection
    @CollectionTable(name = "user_pokemon", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "pokemon_id")
    private Set<Integer> pokemonList = new HashSet<>();

    // Costruttore, getter e setter omessi per brevità...

    // Metodo per aggiungere un Pokemon alla lista dei Pokemon dell'utente
    public void addPokemonToPokemonList(int pokemonId) {
        this.pokemonList.add(pokemonId);
    }


    public User(String username, String password, String email, String name, String surname, String avatarUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
