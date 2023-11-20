package changer.pitagoras.controller;

import changer.pitagoras.dto.UsuarioAdmDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.service.ChangerService;
import changer.pitagoras.service.UsuarioService;
import changer.pitagoras.util.ListaObj;
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
import java.util.List;

@RestController
@RequestMapping("/changer")
public class ChangerController {

    @Autowired
    ChangerService changerService;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/adm/download-csv")
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

    @GetMapping("/adm/download-txt")
    public ResponseEntity<InputStreamResource> downloadTXT() throws FileNotFoundException {
        String filename = "Usuarios.txt";
        changerService.exportaUsuarioParaTXT(filename); // Correção aqui

        File file = new File(filename);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentType(MediaType.parseMediaType("application/txt"))
                .body(resource);
    }

    @GetMapping("/adm/usuarios")
    public ResponseEntity<List<UsuarioAdmDto>> listarUsuarios() {
        List<UsuarioAdmDto> lista = usuarioService.listarUsuariosAdm();

        if (lista.isEmpty())
            return ResponseEntity.status(204).build();

        return ResponseEntity.status(200).body(lista);
    }
}
