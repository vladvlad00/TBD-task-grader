package ro.uaic.info.taskgrader.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskgrader.entity.Grade;
import ro.uaic.info.taskgrader.entity.GradePK;
import ro.uaic.info.taskgrader.repository.GradeRepository;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/grade")
public class GradeController {
    @Autowired
    private GradeRepository gradeRepository;

    // TODO Trebuie facut ceva frumos aici, nu hardcodat
    private final String BASE_URL = "https://tbd-dev.herokuapp.com/";

    private Integer calculateGrade(Integer studentId, Integer taskId) {
        var restTemplate = new RestTemplate();
        String url = BASE_URL + "score_answer/task_student/" + taskId + "/" + studentId;
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (HttpStatusCodeException e) {
            return null;
        }
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                XmlMapper mapper = new XmlMapper();
                List<Map<String, Object>> gradeJsons = mapper.readValue(response.getBody(),
                        new TypeReference<List<Map<String, Object>>>() {
                        });
                Integer grade = 0;
                for (var gradeJson : gradeJsons) {
                    grade += Integer.parseInt((String) gradeJson.get("scoreValue"));
                }
                return grade;
            } catch (JsonProcessingException e) {
                return null;
            }

        } else
            return null;
    }

    @PostMapping("/")
    private ResponseEntity<Grade> createGrade(@RequestBody Map<String, String> gradeJson) {
        Integer studentId;
        Integer taskId;
        Integer grade = null;

        try {
            studentId = Integer.parseInt(gradeJson.get("studentId"));
            taskId = Integer.parseInt(gradeJson.get("taskId"));
            if (gradeJson.get("grade") != null)
                grade = Integer.parseInt(gradeJson.get("grade"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        GradePK id = new GradePK(taskId, studentId);
        if (gradeRepository.findById(id).isPresent())
            return ResponseEntity.badRequest().build();

        if (grade == null) {
            grade = calculateGrade(studentId, taskId);

            if (grade == null)
                return ResponseEntity.badRequest().build();
        }


        Grade gradeObj = new Grade();
        gradeObj.setId(new GradePK(taskId, studentId));
        gradeObj.setGrade(grade);
        gradeObj = gradeRepository.save(gradeObj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{taskId}/{studentId}").buildAndExpand(taskId, studentId).toUri();
        return ResponseEntity.created(uri).body(gradeObj);
    }

    @GetMapping("/task/{taskId}/student/{studentId}")
    private ResponseEntity<Grade> listGradesByTaskStudentId(@PathVariable Integer taskId, @PathVariable Integer studentId) {
        Optional<Grade> gradeOpt = gradeRepository.findById(new GradePK(taskId, studentId));
        if (gradeOpt.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(gradeOpt.get());
    }

    @GetMapping("/task/{taskId}")
    private ResponseEntity<Iterable<Grade>> listGradesByTaskId(@PathVariable Integer taskId) {
        Iterable<Grade> foundGrades = gradeRepository.findByTaskId(taskId);
        int count = 0;
        for (var grade : foundGrades)
            count++;
        if (count == 0)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundGrades);
    }

    @GetMapping("/student/{studentId}")
    private ResponseEntity<Iterable<Grade>> listGradesByStudentId(@PathVariable Integer studentId) {
        Iterable<Grade> foundGrades = gradeRepository.findByStudentId(studentId);
        int count = 0;
        for (var grade : foundGrades)
            count++;
        if (count == 0)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundGrades);
    }

    @PutMapping("/task/{taskIdPath}/student/{studentIdPath}")
    private ResponseEntity<Grade> updateGrade(@RequestBody Map<String, String> gradeJson,
                                              @PathVariable Integer taskIdPath, @PathVariable Integer studentIdPath) {
        Integer studentId;
        Integer taskId;
        Integer grade;

        try {
            studentId = Integer.parseInt(gradeJson.get("studentId"));
            taskId = Integer.parseInt(gradeJson.get("taskId"));
            grade = Integer.parseInt(gradeJson.get("grade"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (!studentId.equals(studentIdPath) || !taskId.equals(taskIdPath))
            return ResponseEntity.badRequest().build();

        GradePK id = new GradePK(taskId, studentId);
        Optional<Grade> gradeOpt = gradeRepository.findById(id);
        if (gradeOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Grade gradeObj = gradeOpt.get();
        gradeObj.setGrade(grade);

        gradeObj = gradeRepository.save(gradeObj);

        return ResponseEntity.ok(gradeObj);
    }

    @DeleteMapping("/task/{taskId}/student/{studentId}")
    private ResponseEntity<Grade> deleteGrade(@PathVariable Integer taskId, @PathVariable Integer studentId) {
        GradePK id = new GradePK(taskId, studentId);
        if (gradeRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();
        gradeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
