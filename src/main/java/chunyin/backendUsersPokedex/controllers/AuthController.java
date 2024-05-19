package chunyin.backendUsersPokedex.controllers;

import chunyin.backendUsersPokedex.exceptions.BadRequestException;
import chunyin.backendUsersPokedex.payloads.NewUserDTO;
import chunyin.backendUsersPokedex.payloads.NewUserRespDTO;
import chunyin.backendUsersPokedex.payloads.UserLoginDTO;
import chunyin.backendUsersPokedex.payloads.UserLoginRespDTO;
import chunyin.backendUsersPokedex.services.AuthService;
import chunyin.backendUsersPokedex.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginRespDTO login(@RequestBody UserLoginDTO payload){

        return new UserLoginRespDTO(this.authService.authenticateUtenteAndGenerateToken(payload), this.authService.getId(payload));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO saveUser(@RequestBody @Validated NewUserDTO body, BindingResult validation) throws IOException {

        if(validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        }
        return new NewUserRespDTO(this.userService.save(body).getId());
    }

    //LOGOUT DA TESTARE

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}