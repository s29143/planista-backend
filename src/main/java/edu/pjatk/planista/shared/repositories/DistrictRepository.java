package edu.pjatk.planista.shared.repositories;

import edu.pjatk.planista.shared.models.District;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "districts"
)
public interface DistrictRepository extends DictItemRepository<District> {
}
