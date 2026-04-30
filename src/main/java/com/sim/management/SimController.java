package com.sim.management;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

@RestController
@RequestMapping("/sims")
@CrossOrigin(origins = "*")
public class SimController {

    // Temporary in-memory storage (instead of DB)
    private List<Sim> sims = new ArrayList<>();

    // ✅ GET all sims
    @GetMapping
    public List<Sim> getAllSims() {
        return sims;
    }

    // ✅ ADD SIM
    @PostMapping
    public ResponseEntity<?> addSim(@RequestBody Sim sim) {

        for (Sim s : sims) {
            if (s.getMobileNo().equals(sim.getMobileNo())) {
                return ResponseEntity.badRequest().body("Mobile number already exists");
            }
        }

        sims.add(sim);
        return ResponseEntity.ok(sim);
    }

    // ✅ UPLOAD FILE (Excel)
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
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

                boolean exists = sims.stream()
                        .anyMatch(s -> s.getMobileNo().equals(mobileNo));

                if (!exists) {
                    sims.add(sim);
                }
            }

            workbook.close();
            return "File uploaded successfully";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ✅ DELETE SIM
    @DeleteMapping("/{id}")
    public String deleteSim(@PathVariable int id) {
        if (id >= 0 && id < sims.size()) {
            sims.remove(id);
            return "Deleted successfully";
        }
        return "Invalid ID";
    }

    // ✅ UPDATE SIM
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSim(@PathVariable int id, @RequestBody Sim simDetails) {

        if (id < 0 || id >= sims.size()) {
            return ResponseEntity.badRequest().body("Invalid ID");
        }

        Sim sim = sims.get(id);

        sim.setProvider(simDetails.getProvider());
        sim.setMobileNo(simDetails.getMobileNo());
        sim.setOrgName(simDetails.getOrgName());

        return ResponseEntity.ok(sim);
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public List<Sim> searchByMobile(@RequestParam String mobileNo) {
        List<Sim> result = new ArrayList<>();

        for (Sim sim : sims) {
            if (sim.getMobileNo().contains(mobileNo)) {
                result.add(sim);
            }
        }

        return result;
    }
}