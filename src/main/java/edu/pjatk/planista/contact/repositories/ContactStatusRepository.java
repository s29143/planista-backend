package edu.pjatk.planista.contact.repositories;

import edu.pjatk.planista.contact.models.ContactStatus;
import edu.pjatk.planista.shared.repositories.DictItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "contact-statuses",
        collectionResourceRel = "contact-statuses"
)
public interface ContactStatusRepository extends DictItemRepository<ContactStatus> {
}
