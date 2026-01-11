package edu.pjatk.planista.shared.kernel.ports;

import edu.pjatk.planista.company.models.Company;

public interface CompanyQueryPort {
    Company getReferenceById(Long id);
}
