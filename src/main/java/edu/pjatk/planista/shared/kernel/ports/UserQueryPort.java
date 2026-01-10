package edu.pjatk.planista.shared.kernel.ports;

import edu.pjatk.planista.shared.models.AppUser;

public interface UserQueryPort {
    AppUser getReferenceById(Long id);
}
