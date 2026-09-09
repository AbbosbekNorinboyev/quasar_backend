package uz.brbtech.quasar_backend.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.brbtech.quasar_backend.dto.search.RoleSearchRequest;
import uz.brbtech.quasar_backend.entity.RoleEntity;

import java.util.ArrayList;
import java.util.List;

public class RoleSpecification {
    public static Specification<RoleEntity> search(RoleSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getId() != null && request.getId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }

            if (request.getName() != null && !request.getName().isBlank()) {
                String search = request.getName()
                        .trim()
                        .toLowerCase();
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + search + "%"
                        )
                );
            }

            if (request.getStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                request.getStatus()
                        )
                );
            }

            if (request.getCreatedAt() != null && !request.getCreatedAt().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.function(
                                        "to_char",
                                        String.class,
                                        root.get("createdAt"),
                                        criteriaBuilder.literal("DD.MM.YYYY HH24:MI")
                                ),
                                "%" + request.getCreatedAt().trim() + "%"
                        )
                );
            }

            if (request.getUpdatedAt() != null && !request.getUpdatedAt().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.function(
                                        "to_char",
                                        String.class,
                                        root.get("updatedAt"),
                                        criteriaBuilder.literal("DD.MM.YYYY HH24:MI")

                                ),
                                "%" + request.getUpdatedAt().trim() + "%"
                        )
                );
            }

            // Hech qanday search berilmagan bo'lsa
            // barcha recordlarni qaytaradi
            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Birorta field mos kelsa yetarli (OR logic)
            // Search qo'yilgan field mos kelsa yetarli (AND logic)
            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}