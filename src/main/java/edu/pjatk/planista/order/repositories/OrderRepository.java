package edu.pjatk.planista.order.repositories;

import edu.pjatk.planista.order.models.Order;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findByCompanyId(Long companyId, Pageable pageable);
    Page<Order> findByContactId(Long contactId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "type",
            "company",
            "contact",
            "status"
    })
    @Override
    Page<Order> findAll(Specification<Order> specification, @NonNull Pageable pageable);

    @Query("""
                select o from Order o
                where o.dateFrom is not null
                  and o.dateFrom <= :to
                  and (o.dateTo is null or o.dateTo >= :from)
            """)
    List<Order> findForGantt(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
