package com.sim.management;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SimRepository extends JpaRepository<Sim, Long> {

    boolean existsByMobileNo(String mobileNo);

    List<Sim> findByMobileNo(String mobileNo);
}