package edu.pjatk.planista.company.repositories;

import edu.pjatk.planista.company.models.CompanyStatus;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "company-statuses"
)
public interface CompanyStatusRepository extends DictItemRepository<CompanyStatus> {
}
