package chunyin.backendUsersPokedex.payloads;

import jakarta.validation.constraints.*;

public record NewUserDTO(@NotEmpty(message = "Username is required")
                         @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
                         String username,
                         @NotEmpty(message = "Password is required")
                         @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
                         String password,
                         @NotEmpty(message = "Email is required")
                         @Email(message = "The email is not valid")
                         String email,
                         @NotEmpty(message = "First name is required")
                         @Size(min = 2, max = 20, message = "First name must be between 2 and 20 characters")
                         String name,
                         @NotEmpty(message = "Surname is required")
                         @Size(min = 2, max = 20, message = "Surname must be between 2 and 20 characters")
                         String surname) {
}
