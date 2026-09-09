package uz.brbtech.quasar_backend.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.brbtech.quasar_backend.dto.search.UserSearchRequest;
import uz.brbtech.quasar_backend.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<UserEntity> search(UserSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getId() != null && request.getId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }

            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                String search = request.getFullName()
                        .trim()
                        .toLowerCase();
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("fullName")),
                                "%" + search + "%"
                        )
                );
            }

            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
                String search = request.getPhoneNumber()
                        .trim()
                        .toLowerCase();
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("phoneNumber")),
                                "%" + search + "%"
                        )
                );
            }

            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                String search = request.getEmail()
                        .trim()
                        .toLowerCase();
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("email")),
                                "%" + search + "%"
                        )
                );
            }

            if (request.getUsername() != null && !request.getUsername().isBlank()) {
                String search = request.getUsername()
                        .trim()
                        .toLowerCase();
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("username")),
                                "%" + search + "%"
                        )
                );
            }

            if (request.getBirthDate() != null && !request.getBirthDate().isBlank()) {
                String search = request.getBirthDate()
                        .trim()
                        .toLowerCase();
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("birthDate")),
                                "%" + search + "%"
                        )
                );
            }

            if (request.getRoleId() != null && request.getRoleId() > 0) {
                predicates.add(
                        criteriaBuilder.equal(root.get("roles").get("id"), request.getRoleId())
                );
            }

            if (request.getStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"), request.getStatus())
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