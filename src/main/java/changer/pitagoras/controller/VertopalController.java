package changer.pitagoras.controller;

import changer.pitagoras.service.VertopalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vertopal")
public class VertopalController {
    @Autowired
    VertopalService vertopalService = new VertopalService();

    @PostMapping("/enviar")
    public String enviarArquivo(@RequestParam("file") MultipartFile file){
        return vertopalService.enviarArquivo(file);
    }

    @PostMapping("/converter")
    public String converterArquivo(@RequestParam("extensao")String extensao){
        return vertopalService.converterArquivo(extensao);
    }

    @PostMapping("/baixar")
    public void baixarArquivo(@RequestParam("local-download") String localDownload){
        vertopalService.recuperarArquivo(localDownload);
    }
}
