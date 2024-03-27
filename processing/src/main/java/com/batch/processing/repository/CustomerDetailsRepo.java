package com.batch.processing.repository;

import com.batch.processing.entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDetailsRepo extends JpaRepository<CustomerDetails,Integer>{
}
