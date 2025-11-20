package edu.pjatk.planista.shared.repositories;

import edu.pjatk.planista.shared.models.DictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface DictItemRepository<T extends DictItem> extends JpaRepository<T, Long> {
    Optional<T> findByNameIgnoreCase(String name);
}
