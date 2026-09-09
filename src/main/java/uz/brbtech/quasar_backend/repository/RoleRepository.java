package uz.brbtech.quasar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uz.brbtech.quasar_backend.entity.RoleEntity;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>,
        JpaSpecificationExecutor<RoleEntity> {
    Optional<RoleEntity> findByName(String name);
}