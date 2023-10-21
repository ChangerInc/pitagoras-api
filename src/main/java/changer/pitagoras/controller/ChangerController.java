package changer.pitagoras.controller;

import changer.pitagoras.service.ChangerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/changer/adm")
public class ChangerController {

    @Autowired
    ChangerService changerService;


    @GetMapping("/download-csv")
    public ResponseEntity<InputStreamResource> downloadCSV() throws FileNotFoundException {
        String filename = "Usuarios.csv";
        changerService.exportaUsuarioParaCSV(filename); // Correção aqui

        File file = new File(filename);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }
}
