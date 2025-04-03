package com.deloitte.repositories;

import com.deloitte.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // Additional custom query methods can be added here if needed
}
