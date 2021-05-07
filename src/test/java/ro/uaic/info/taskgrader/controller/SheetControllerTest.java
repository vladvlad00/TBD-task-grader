package ro.uaic.info.taskgrader.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.uaic.info.taskgrader.entity.Sheet;
import ro.uaic.info.taskgrader.repository.SheetRepository;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(SheetController.class)
class SheetControllerTest {

    @MockBean
    SheetRepository sheetRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createSheet_BadRequest() throws Exception {

        Sheet testInvalidSheet = new Sheet();
        testInvalidSheet.setName("testInvalidSheet");
        testInvalidSheet.setId(0);
        when(sheetRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(testInvalidSheet));

        mockMvc.perform(post("/sheet/")
                .contentType(mapper.writeValueAsString(testInvalidSheet))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        // bad request case - testInvalidSheet.id != null && and sheet already exists //



    }

    @Test
    void createSheet_Valid() throws Exception{
        Sheet testValidSheet = new Sheet();
        testValidSheet.setName("testSheet");
        testValidSheet.setId(null);
        when(sheetRepository.save(any(Sheet.class))).thenReturn(testValidSheet);
        mockMvc.perform(post("/sheet/")
                .content(mapper.writeValueAsString(testValidSheet))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(testValidSheet.getName()));
        // case in which we successfully create a sheet //
    }

    @Test
    void listAllSheets_NotFound() throws Exception {
        List<Sheet> foundSheets1 = null;
        when(sheetRepository.findAll()).thenReturn(foundSheets1);

        mockMvc.perform(get("/sheet/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        // status not found case //

    }

    @Test
    void listAllSheets_Valid() throws Exception {
        Sheet testValidSheet1 = new Sheet();
        testValidSheet1.setName("testValidSheet1");

        Sheet testValidSheet2 = new Sheet();
        testValidSheet2.setName("testValidSheet2");

        List<Sheet> foundSheets2 = new ArrayList<>();

        foundSheets2.add(testValidSheet1);
        foundSheets2.add(testValidSheet2);

        when(sheetRepository.findAll()).thenReturn(foundSheets2);

        mockMvc.perform(get("/sheet/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        // case in which we successfully retrieve all sheets //
    }

    @Test
    void listSheet_NotFound() throws Exception {

        when(sheetRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/sheet/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        // status not found case //

    }

    @Test
    void listSheet_Valid() throws Exception {
        Sheet testSheet = new Sheet();
        testSheet.setName("testSheet");
        testSheet.setId(0);

        when(sheetRepository.findById(any(Integer.class))).thenReturn(Optional.of(testSheet));

        mockMvc.perform(get("/sheet/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        // case in which we successfully find the sheet //
    }

    @Test
    void updateSheet_BadRequest1() throws Exception {
        Sheet testInvalidSheet1 = new Sheet();
        testInvalidSheet1.setId(null);
        testInvalidSheet1.setName("testInvalidSheet1");

        mockMvc.perform(put("/sheet/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidSheet1)))
                .andExpect(status().isBadRequest());
        // case 1 of bad request - testInvalidSheet1.id == null //

    }

    @Test
    void updateSheet_BadRequest2() throws Exception {
        Sheet testInvalidSheet2 = new Sheet();
        testInvalidSheet2.setId(1);
        testInvalidSheet2.setName("testInvalidSheet2");

        mockMvc.perform(put("/sheet/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidSheet2)))
                .andExpect(status().isBadRequest());
        // case 2 of bad request - testInvalidSheet2.id != {id} //

    }

    @Test
    void updateSheet_NotFound() throws Exception {
        when(sheetRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Sheet testInvalidSheet3 = new Sheet();
        testInvalidSheet3.setId(0);
        testInvalidSheet3.setName("testInvalidSheet3");

        mockMvc.perform(put("/sheet/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidSheet3)))
                .andExpect(status().isNotFound());
        // status not found case - couldn't find a sheet with the given id to update //


    }

    @Test
    void updateSheet_Valid() throws Exception {
        Sheet testValidSheet = new Sheet();
        testValidSheet.setId(0);
        testValidSheet.setName("testValidSheet");
        when(sheetRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidSheet));
        when(sheetRepository.save(any(Sheet.class))).thenReturn(testValidSheet);
        mockMvc.perform(put("/sheet/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testValidSheet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testValidSheet.getName()))
                .andExpect(jsonPath("$.id").value(testValidSheet.getId()));
        // case in which we successfully update the sheet with the given id //
    }

    @Test
    void deleteSheet_NotFound() throws Exception {
        when(sheetRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/sheet/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        // status not found case - couldn't find a sheet with the given id to delete //

    }

    @Test
    void deleteSheet_Valid() throws Exception {
        Sheet testValidSheet = new Sheet();
        testValidSheet.setId(0);
        testValidSheet.setName("testValidSheet");
        when(sheetRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidSheet));
        mockMvc.perform(delete("/sheet/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // case in which we successfully delete the sheet with the given id //
    }
}