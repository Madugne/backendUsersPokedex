package chunyin.backendUsersPokedex.exceptions;

import java.time.LocalDateTime;

public record ErrorsResponsePayload(String message, LocalDateTime timestamp) {
}
