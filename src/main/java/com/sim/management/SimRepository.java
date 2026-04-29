package com.sim.management;
import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SimRepository extends JpaRepository<Sim, Long> {
	List<Sim> findByMobileNo(String mobileNo);
	 boolean existsByMobileNo(String mobileNo);

}
