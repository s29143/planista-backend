package edu.pjatk.planista.company.specs;

import edu.pjatk.planista.company.models.Company;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Expression;
import java.util.Collection;
import java.util.List;

public final class CompanySpecs {

    private CompanySpecs() {}

    public static Specification<Company> userIdIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return (root, cq, cb) -> root.join("user").get("id").in(ids);
    }

    public static Specification<Company> statusIdIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return (root, cq, cb) -> root.join("status").get("id").in(ids);
    }

    public static Specification<Company> searchLike(String search) {
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
}
