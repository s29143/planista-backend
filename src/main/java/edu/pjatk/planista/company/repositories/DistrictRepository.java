package edu.pjatk.planista.company.repositories;

import edu.pjatk.planista.company.models.District;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "districts"
)
public interface DistrictRepository extends DictItemRepository<District> {
}
