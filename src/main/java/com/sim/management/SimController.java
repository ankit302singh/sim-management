package com.sim.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.List;

@RestController
@RequestMapping("/sims")
@CrossOrigin(origins = "*")
public class SimController {

    @Autowired
    private SimRepository simRepository;

    // ✅ GET all sims
    @GetMapping
    public List<Sim> getAllSims() {
        return simRepository.findAll();
    }

    // ✅ ADD SIM (with duplicate check)
    @PostMapping
    public ResponseEntity<?> addSim(@RequestBody Sim sim) {

        if (simRepository.existsByMobileNo(sim.getMobileNo())) {
            return ResponseEntity
                    .badRequest()
                    .body("Mobile number already exists");
        }

        Sim saved = simRepository.save(sim);
        return ResponseEntity.ok(saved);
    }
    
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            List<Sim> sims = new ArrayList<>();

            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                Sim sim = new Sim();

                Cell cell = row.getCell(0);

                String mobileNo;

                if (cell.getCellType() == CellType.NUMERIC) {
                    mobileNo = String.valueOf((long) cell.getNumericCellValue());
                } else {
                    mobileNo = cell.getStringCellValue();
                }

                sim.setMobileNo(mobileNo);
                sim.setProvider(row.getCell(1).getStringCellValue());
                sim.setOrgName(row.getCell(2).getStringCellValue());

                if (!simRepository.existsByMobileNo(sim.getMobileNo())) {
                    sims.add(sim);
                }
            }

            simRepository.saveAll(sims);
            workbook.close();

            return "File uploaded successfully";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ✅ DELETE SIM
    @DeleteMapping("/{id}")
    public String deleteSim(@PathVariable Long id) {
        simRepository.deleteById(id);
        return "Deleted successfully";
    }

    // ✅ UPDATE SIM
    @PutMapping("/{id}")
    public Sim updateSim(@PathVariable Long id, @RequestBody Sim simDetails) {

        Sim sim = simRepository.findById(id).orElseThrow();

        sim.setProvider(simDetails.getProvider());
        sim.setMobileNo(simDetails.getMobileNo());
        sim.setOrgName(simDetails.getOrgName());

        return simRepository.save(sim);
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public List<Sim> searchByMobile(@RequestParam String mobileNo) {
        return simRepository.findByMobileNo(mobileNo);
    }
}