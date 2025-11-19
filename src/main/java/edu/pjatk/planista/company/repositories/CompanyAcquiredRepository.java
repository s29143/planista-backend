package edu.pjatk.planista.company.repositories;

import edu.pjatk.planista.company.models.CompanyAcquired;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    path = "company-acquires"
)
public interface CompanyAcquiredRepository extends DictItemRepository<CompanyAcquired> {
}
