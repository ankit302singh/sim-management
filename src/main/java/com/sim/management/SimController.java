package com.sim.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

@RestController
@RequestMapping("/sims")
@CrossOrigin(origins = "*")
public class SimController {

    @Autowired
    private SimService simService;

    // ✅ GET all sims
    @GetMapping
    public List<Sim> getAllSims() {
        return simService.getAll();
    }

    // ✅ ADD SIM
    @PostMapping
    public Sim addSim(@RequestBody Sim sim) {
        return simService.add(sim);
    }

    // ✅ UPLOAD EXCEL
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

                sims.add(sim);
            }

            for (Sim s : sims) {
                simService.add(s);
            }

            workbook.close();
            return "File uploaded successfully";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String deleteSim(@PathVariable Long id) {
        simService.delete(id);
        return "Deleted successfully";
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public Sim updateSim(@PathVariable Long id, @RequestBody Sim simDetails) {
        return simService.update(id, simDetails);
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public List<Sim> search(@RequestParam String mobileNo) {
        return simService.search(mobileNo);
    }
}