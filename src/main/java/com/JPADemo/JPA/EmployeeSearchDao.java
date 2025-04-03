package com.JPADemo.JPA;

import com.JPADemo.JPA.models.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class EmployeeSearchDao {

    private final EntityManager em;

    public List<Employee> findAllBySimpleQuery(
            String firstName,
            String lastName,
            String email
    ) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        // select * from employee
        Root<Employee> root = criteriaQuery.from(Employee.class);

        // prepare WHERE clause
        // WHERE first name like '%mahfuj%'
        Predicate firstNamePredicate = criteriaBuilder
                .like(root.get("firstName"), Pattern:"%" + firstName + "%" );
        Predicate lastNamePredicate = criteriaBuilder
                .like(root.get("lastName"), Pattern:"%" + lastName + "%");
        Predicate emailPredicate = criteriaBuilder
                .like(root.get("email"), Pattern:"%" + email + "%");
        Predicate firstNameOrlastNamePredicate = criteriaBuilder.or(
                firstNamePredicate,
                lastNamePredicate
        );
        // => final query ==> select * from employee where firstName like '%mahfuj%'
        // or lastName like '%mahfuj%'
        var andEmailPredicate: Predicate = CriteriaBuilder.and(firstNameOrlastNamePredicate, emailPredicate);
        criteriaQuery.where(andEmailPredicate);
        TypedQuery<Employee> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
