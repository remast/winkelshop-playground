package remast.winkelshop.auth.boundary;

import java.util.UUID;

public record User(UUID userId, String role) {
}
