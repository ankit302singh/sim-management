package com.sim.management;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SimRepository simRepository;

    // ================= GET ALL =================
    @GetMapping
    public List<Sim> getAll() {
        return simRepository.findAll();
    }

    // ================= ADD =================
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Sim sim) {
        if (simRepository.existsByMobileNo(sim.getMobileNo())) {
            return ResponseEntity.badRequest().body("Already exists");
        }
        return ResponseEntity.ok(simRepository.save(sim));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        simRepository.deleteById(id);
        return "Deleted";
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public Sim update(@PathVariable Long id, @RequestBody Sim simDetails) {
        Sim sim = simRepository.findById(id).orElseThrow();

        sim.setProvider(simDetails.getProvider());
        sim.setMobileNo(simDetails.getMobileNo());
        sim.setOrgName(simDetails.getOrgName());

        return simRepository.save(sim);
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public List<Sim> search(@RequestParam String mobileNo) {
        return simRepository.findByMobileNo(mobileNo);
    }

    // ================= UPLOAD EXCEL =================
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
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

            return ResponseEntity.ok("Upload successful: " + sims.size() + " records added");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }
}