package com.sim.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/sims")
@CrossOrigin(origins = "*")
public class SimController {

    @Autowired
    private SimRepository simRepository;

    @GetMapping
    public List<Sim> getAll() {
        return simRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Sim sim) {
        if (simRepository.existsByMobileNo(sim.getMobileNo())) {
            return ResponseEntity.badRequest().body("Already exists");
        }
        return ResponseEntity.ok(simRepository.save(sim));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        simRepository.deleteById(id);
        return "Deleted";
    }

    @PutMapping("/{id}")
    public Sim update(@PathVariable Long id, @RequestBody Sim simDetails) {
        Sim sim = simRepository.findById(id).orElseThrow();

        sim.setProvider(simDetails.getProvider());
        sim.setMobileNo(simDetails.getMobileNo());
        sim.setOrgName(simDetails.getOrgName());

        return simRepository.save(sim);
    }

    @GetMapping("/search")
    public List<Sim> search(@RequestParam String mobileNo) {
        return simRepository.findByMobileNo(mobileNo);
    }
}