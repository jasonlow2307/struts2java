package com.deloitte.struts2java.repository;


import com.deloitte.struts2java.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // Additional custom queries can be added here if needed
}