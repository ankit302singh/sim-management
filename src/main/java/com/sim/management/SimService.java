package com.sim.management;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SimService {

    private final List<Sim> sims = new ArrayList<>();
    private long currentId = 1;

    public List<Sim> getAll() {
        return sims;
    }

    public Sim add(Sim sim) {
        sim.setId(currentId++);
        sims.add(sim);
        return sim;
    }

    public void delete(Long id) {
        sims.removeIf(sim -> sim.getId().equals(id));
    }

    public Sim update(Long id, Sim updated) {
        for (Sim sim : sims) {
            if (sim.getId().equals(id)) {
                sim.setProvider(updated.getProvider());
                sim.setMobileNo(updated.getMobileNo());
                sim.setOrgName(updated.getOrgName());
                return sim;
            }
        }
        throw new RuntimeException("Not found");
    }

    public List<Sim> search(String mobileNo) {
        List<Sim> result = new ArrayList<>();
        for (Sim sim : sims) {
            if (sim.getMobileNo().contains(mobileNo)) {
                result.add(sim);
            }
        }
        return result;
    }
}