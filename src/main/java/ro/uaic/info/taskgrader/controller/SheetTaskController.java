package ro.uaic.info.taskgrader.controller;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskgrader.entity.Sheet;
import ro.uaic.info.taskgrader.entity.SheetTask;
import ro.uaic.info.taskgrader.entity.SheetTaskPK;
import ro.uaic.info.taskgrader.repository.SheetRepository;
import ro.uaic.info.taskgrader.repository.SheetTaskRepository;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path="/sheet_task")
public class SheetTaskController {
    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private SheetTaskRepository sheetTaskRepository;

    private final String BASE_URL="https://tbd-dev.herokuapp.com/";

    @PostMapping("/")
    private ResponseEntity<SheetTask> createSheetTask(@RequestBody Map<String,Integer> sheetTask){
        Integer sheetId=sheetTask.get("sheetId");
        Integer taskId=sheetTask.get("taskId");
        if(sheetId==null||taskId==null){
            return ResponseEntity.badRequest().build();
        }

        var restTemplate=new RestTemplate();
        String url=BASE_URL+"task/"+taskId;
        System.out.println(url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if(response.getStatusCode().is4xxClientError()){
            return ResponseEntity.badRequest().build();
        }
        ///throws 500 instead idk y

        Optional<Sheet> sheetOpt=sheetRepository.findById(sheetId);

        if(sheetOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        SheetTaskPK id=new SheetTaskPK(sheetId,taskId);
        SheetTask newSheetTask=new SheetTask();
        newSheetTask.setId(id);
        newSheetTask=sheetTaskRepository.save(newSheetTask);

        URI uri= ServletUriComponentsBuilder.fromCurrentRequest().path("/{sheetId}/{taskId}")
                .buildAndExpand(sheetId,taskId).toUri();
        return ResponseEntity.created(uri).body(newSheetTask);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<SheetTask>> listAllSheetTasks(){
        Iterable<SheetTask> foundSheetTasks=sheetTaskRepository.findAll();
        if(foundSheetTasks==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundSheetTasks);
    }

    @GetMapping("/sheet/{sheet_id}")
    public ResponseEntity<Iterable<SheetTask>> listBySheetId(@PathVariable Integer sheet_id){
        var foundSheetTask=sheetTaskRepository.findAllByIdSheetId(sheet_id);

        if(((Collection<?>)foundSheetTask).size()==0)
            return ResponseEntity.notFound().build();

        var ans=new ArrayList<Map<String,Object>>();

        return ResponseEntity.ok(foundSheetTask);
    }

    @DeleteMapping("/")
    public ResponseEntity<SheetTask> deleteSheetTask(@RequestBody Map<String, Integer> sheetTask){
        Integer sheetId=sheetTask.get("sheetId");
        Integer taskId=sheetTask.get("taskId");

        if(sheetId==null||taskId==null){
            return ResponseEntity.badRequest().build();
        }

        Optional<SheetTask> sheetTaskOpt=sheetTaskRepository.findById((new SheetTaskPK(sheetId,taskId)));

        if(sheetTaskOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        sheetTaskRepository.deleteById((new SheetTaskPK(sheetId,taskId)));
        return ResponseEntity.noContent().build();
    }

}
