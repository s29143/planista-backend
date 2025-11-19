package edu.pjatk.planista.shared.repositories;

import edu.pjatk.planista.shared.models.DictItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DictItemRepository<T extends DictItem> extends JpaRepository<T, Long> {
    Optional<T> findByNameIgnoreCase(String name);
}
