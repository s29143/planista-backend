package edu.pjatk.planista.contact.specs;

import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.contact.models.Contact;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

public final class ContactSpecs {

    private ContactSpecs() {}

    public static Specification<Contact> userIdIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return (root, cq, cb) -> root.join("user").get("id").in(ids);
    }

    public static Specification<Contact> statusIdIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return (root, cq, cb) -> root.join("status").get("id").in(ids);
    }

    public static Specification<Contact> searchCompaniesLike(String search) {
        if (search == null || search.isBlank()) return null;

        final List<String> tokens = List.of(search.trim().toLowerCase().split("\\s+"));

        return (root, cq, cb) -> {
            Expression<String> shortName = cb.lower(root.get("shortName"));
            Expression<String> fullName  = cb.lower(root.get("fullName"));

            return tokens.stream()
                    .map(tok -> {
                        String like = "%" + tok + "%";
                        return cb.or(
                                cb.like(shortName, like),
                                cb.like(fullName, like)
                        );
                    })
                    .reduce(cb::and)
                    .orElseGet(cb::conjunction);
        };
    }

    public static Specification<Contact> searchLike(String search) {
        if (search == null || search.isBlank()) return null;

        final List<String> tokens = List.of(search.trim().toLowerCase().split("\\s+"));

        return (root, cq, cb) -> {
            Expression<String> firstName = cb.lower(root.get("firstName"));
            Expression<String> lastName  = cb.lower(root.get("lastName"));
            Expression<String> email  = root.get("email");

            return tokens.stream()
                    .map(tok -> {
                        String like = "%" + tok + "%";
                        return cb.or(
                                cb.like(firstName, like),
                                cb.like(lastName, like),
                                cb.like(email, like)
                        );
                    })
                    .reduce(cb::and)
                    .orElseGet(cb::conjunction);
        };
    }
}
