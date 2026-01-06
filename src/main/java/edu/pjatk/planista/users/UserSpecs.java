package edu.pjatk.planista.users;

import edu.pjatk.planista.shared.models.AppUser;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public final class UserSpecs {

    private UserSpecs() {}

    public static Specification<AppUser> searchLike(String search) {
        if (search == null || search.isBlank()) return null;

        final List<String> tokens = List.of(search.trim().toLowerCase().split("\\s+"));

        return (root, cq, cb) -> {
            Expression<String> username = cb.lower(root.get("username"));
            Expression<String> firstname  = cb.lower(root.get("firstname"));
            Expression<String> lastname  = root.get("lastname");

            return tokens.stream()
                    .map(tok -> {
                        String like = "%" + tok + "%";
                        return cb.or(
                                cb.like(username, like),
                                cb.like(firstname, like),
                                cb.like(lastname, like)
                        );
                    })
                    .reduce(cb::and)
                    .orElseGet(cb::conjunction);
        };
    }
}
