package lt.brilingas.guestregistry.dao.impl.dao;
import com.fasterxml.jackson.core.JsonProcessingException;
import lt.brilingas.guestregistry.dao.api.IWorkerDAO;
import lt.brilingas.guestregistry.dao.api.ParameterNotValidException;
import lt.brilingas.guestregistry.dao.api.QueryParameterFunction;
import lt.brilingas.guestregistry.dao.impl.IQueryBuilder;
import lt.brilingas.guestregistry.dao.impl.entity.worker.WorkerEntity;
import lt.brilingas.guestregistry.dao.impl.mapper.WorkerMapper;
import lt.brilingas.guestregistry.dao.impl.repository.IWorkerRepository;
import lt.brilingas.guestregistry.data.dto.worker.WorkerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WorkerDAO implements IWorkerDAO {
    @Autowired
    private IWorkerRepository workerRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IQueryBuilder queryBuilder;

    @Override
    public String insert(WorkerDTO workerDTO) {
        WorkerEntity workerEntity = WorkerMapper.toEntity(workerDTO);
        WorkerEntity savedEntity = workerRepository.insert(workerEntity);
        return savedEntity.getId();
    }

    @Override
    public void update(WorkerDTO workerDTO) {
        WorkerEntity workerEntity = WorkerMapper.toEntity(workerDTO);
        workerRepository.save(workerEntity);
    }

    @Override
    public void deleteById(String workerId) {
        workerRepository.deleteById(workerId);
    }

    @Override
    public Optional<WorkerDTO> getById(String workerId) {
        return workerRepository.findById(workerId).map(WorkerMapper::toDTO);
    }

    @Override
    public List<WorkerDTO> getAll() {
        List<WorkerEntity> listEntity = workerRepository.findAll();
        return WorkerMapper.toDTOLinkedList(listEntity);
    }

    @Override
    public List<WorkerDTO> getByFilter(Map<String, Map<QueryParameterFunction, String>> parameters)
            throws JsonProcessingException, ParameterNotValidException {
        BasicQuery query = queryBuilder.build(parameters, WorkerDTO.FIELDS_ALLOWED_IN_FILTER);
        List<WorkerEntity> listEntity = mongoTemplate.find(query, WorkerEntity.class);
        return WorkerMapper.toDTOLinkedList(listEntity);
    }

    @Override
    public boolean existsById(String workerId) {
        return workerRepository.existsById(workerId);
    }
}
