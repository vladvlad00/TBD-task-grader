package ro.uaic.info.taskgrader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskgrader.entity.Sheet;
import ro.uaic.info.taskgrader.repository.SheetRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "/sheet")
public class SheetController
{

    @Autowired
    private SheetRepository sheetRepository;

    @PostMapping
    public ResponseEntity<Sheet> createSheet(@RequestBody Sheet sheet)
    {
        if (sheet.getId() != null && sheetRepository.findById(sheet.getId()).isPresent())
            return ResponseEntity.badRequest().build();

        Sheet createdSheet = sheetRepository.save(sheet);


        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdSheet.getId()).toUri();

        return ResponseEntity.created(uri).body(createdSheet);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Sheet>> listAllSheets()
    {
        Iterable<Sheet> foundSheets = sheetRepository.findAll();
        return ResponseEntity.ok(foundSheets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sheet> listSheet(@PathVariable Integer id)
    {
        Optional<Sheet> foundSheet = sheetRepository.findById(id);
        if (foundSheet.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundSheet.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sheet> updateSheet(@RequestBody Sheet sheet, @PathVariable Integer id)
    {
        if (sheet.getId() == null || !sheet.getId().equals(id))
            return ResponseEntity.badRequest().build();

        if (sheetRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        Sheet updatedSheet = sheetRepository.save(sheet);

        return ResponseEntity.ok(updatedSheet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Sheet> deleteSheet(@PathVariable Integer id)
    {
        if (sheetRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        sheetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
