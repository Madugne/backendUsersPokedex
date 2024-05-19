package chunyin.backendUsersPokedex.payloads;

import java.util.UUID;

public record UserLoginRespDTO(String accessToken, UUID id) {
}
