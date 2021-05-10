package ro.uaic.info.taskgrader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskgrader.entity.Sheet;
import ro.uaic.info.taskgrader.entity.SheetStudent;
import ro.uaic.info.taskgrader.entity.SheetStudentPK;
import ro.uaic.info.taskgrader.repository.SheetRepository;
import ro.uaic.info.taskgrader.repository.SheetStudentRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/sheet_student")
public class SheetStudentController {
    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private SheetStudentRepository sheetStudentRepository;

    private final String BASE_URL = "https://tbd-dev.herokuapp.com/";

    @PostMapping("/")
    private ResponseEntity<SheetStudent> createSheetStudent(@RequestBody Map<String, Integer> sheetStudent) {
        Integer sheetId = sheetStudent.get("sheetId");
        Integer studentId = sheetStudent.get("studentId");

        if (sheetId == null || studentId == null)
            return ResponseEntity.badRequest().build();

        SheetStudentPK id = new SheetStudentPK(sheetId, studentId);
        if (sheetStudentRepository.findById(id).isPresent())
            return ResponseEntity.badRequest().build();

        var restTemplate = new RestTemplate();
        String url = BASE_URL + "student/" + studentId;
        System.out.println(url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode().is4xxClientError()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Sheet> sheetOpt = sheetRepository.findById(sheetId);
        if (sheetOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        SheetStudent newSheetStudent = new SheetStudent();
        newSheetStudent.setId(id);

        newSheetStudent = sheetStudentRepository.save(newSheetStudent);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{sheetId}/{studentId}")
                .buildAndExpand(sheetId, studentId).toUri();

        return ResponseEntity.created(uri).body(newSheetStudent);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<SheetStudent>> listAllSheetTasks() {
        Iterable<SheetStudent> foundSheetStudents = sheetStudentRepository.findAll();
        if (foundSheetStudents == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundSheetStudents);
    }

    @GetMapping("/sheet/{sheet_id}")
    public ResponseEntity<Iterable<SheetStudent>> listBySheetId(@PathVariable Integer sheet_id) {
        var foundSheetStudent = sheetStudentRepository.findAllByIdSheetId(sheet_id);

        if (((Collection<?>) foundSheetStudent).size() == 0)
            return ResponseEntity.notFound().build();

        var ans = new ArrayList<Map<String, Object>>();

        return ResponseEntity.ok(foundSheetStudent);
    }

    @GetMapping("/student/{student_id}")
    public ResponseEntity<Iterable<SheetStudent>> listByStudentId(@PathVariable Integer student_id) {
        var foundSheetStudent = sheetStudentRepository.findAllByIdStudentId(student_id);

        if (((Collection<?>) foundSheetStudent).size() == 0)
            return ResponseEntity.notFound().build();

        var ans = new ArrayList<Map<String, Object>>();

        return ResponseEntity.ok(foundSheetStudent);
    }

    @DeleteMapping("/")
    public ResponseEntity<SheetStudent> deleteSheetTask(@RequestBody Map<String, Integer> sheetTask) {
        Integer sheetId = sheetTask.get("sheetId");
        Integer studentId = sheetTask.get("studentId");

        if (sheetId == null || studentId == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<SheetStudent> sheetStudentOpt = sheetStudentRepository.findById((new SheetStudentPK(sheetId, studentId)));

        if (sheetStudentOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        sheetStudentRepository.deleteById((new SheetStudentPK(sheetId, studentId)));
        return ResponseEntity.noContent().build();
    }
}
