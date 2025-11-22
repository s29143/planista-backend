package edu.pjatk.planista.shared.repositories;

import edu.pjatk.planista.shared.models.DictItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DictItemRepository<T extends DictItem> extends JpaRepository<T, Long> {
    Page<T> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
