package chunyin.backendUsersPokedex.services;

import chunyin.backendUsersPokedex.entities.User;
import chunyin.backendUsersPokedex.exceptions.UnauthorizedException;
import chunyin.backendUsersPokedex.payloads.UserLoginDTO;
import chunyin.backendUsersPokedex.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private JWTTools jwtTools;

    public String authenticateUtenteAndGenerateToken(UserLoginDTO payload){

        User utente = this.userService.findByEmail(payload.email());

        if(bcrypt.matches(payload.password(), utente.getPassword())) {
            return jwtTools.createToken(utente);
        } else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    public UUID getId(UserLoginDTO payload){

        User utente = this.userService.findByEmail(payload.email());

        if(bcrypt.matches(payload.password(), utente.getPassword())) {
            return utente.getId();
        } else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }
}

