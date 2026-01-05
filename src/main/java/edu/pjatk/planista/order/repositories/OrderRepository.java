package edu.pjatk.planista.order.repositories;

import edu.pjatk.planista.order.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findByCompanyId(Long companyId, Pageable pageable);
    Page<Order> findByContactId(Long contactId, Pageable pageable);
}
