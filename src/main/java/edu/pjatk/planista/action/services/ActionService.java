package edu.pjatk.planista.action.services;

import edu.pjatk.planista.action.dto.ActionFilter;
import edu.pjatk.planista.action.dto.ActionRequest;
import edu.pjatk.planista.action.mappers.ActionMapper;
import edu.pjatk.planista.action.models.Action;
import edu.pjatk.planista.action.repositories.ActionRepository;
import edu.pjatk.planista.action.repositories.ActionTypeRepository;
import edu.pjatk.planista.shared.kernel.dto.ActionResponse;
import edu.pjatk.planista.shared.kernel.ports.CompanyQueryPort;
import edu.pjatk.planista.shared.kernel.ports.ContactQueryPort;
import edu.pjatk.planista.shared.kernel.ports.UserQueryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.pjatk.planista.action.specs.ActionSpecs.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ActionService {

    private final ActionRepository actionRepository;
    private final ActionMapper mapper;

    private final ActionTypeRepository typeRepository;
    private final UserQueryPort userQueryPort;
    private final CompanyQueryPort companyQueryPort;
    private final ContactQueryPort contactQueryPort;

    @Transactional(readOnly = true)
    public Page<ActionResponse> list(Pageable pageable, ActionFilter filter) {
        Specification<Action> spec = Specification.allOf(
                userIdIn(filter.userIds()),
                typeIdIn(filter.typeIds()),
                searchCompaniesLike(filter.company()),
                searchLike(filter.search())
        );
        return actionRepository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ActionResponse get(Long id) {
        Action entity = actionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action " + id + " not found"));
        return mapper.toResponse(entity);
    }

    public ActionResponse create(ActionRequest req) {
        Action entity = mapper.toEntity(req);
        Action saved = actionRepository.save(entity);
        applyRelations(entity, req);
        return mapper.toResponse(saved);
    }

    public ActionResponse update(Long id, ActionRequest req) {
        Action entity = actionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action " + id + " not found"));
        mapper.updateEntity(entity, req);
        applyRelations(entity, req);
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!actionRepository.existsById(id)) {
            throw new EntityNotFoundException("Action " + id + " not found");
        }
        actionRepository.deleteById(id);
    }

    private void applyRelations(Action action, ActionRequest req) {
        if (req.userId() != null) {
            action.setUser(userQueryPort.getReferenceById(req.userId()));
        } else {
            throw new IllegalArgumentException("userId is required");
        }

        action.setCompany(req.companyId() != null
                ? companyQueryPort.getReferenceById(req.companyId())
                : null);

        action.setContact(req.contactId() != null
                ? contactQueryPort.getReferenceById(req.contactId())
                : null);

        action.setType(req.typeId() != null
                ? typeRepository.getReferenceById(req.typeId())
                : null);
    }
}