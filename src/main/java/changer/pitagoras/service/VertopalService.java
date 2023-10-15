package changer.pitagoras.service;

import changer.pitagoras.config.VertopalConnector;

public class VertopalService {

    String convertURL = VertopalConnector.CONVERT.getURL();
    // Retorna "https://api.vertopal.com/v1/convert/file"
    String downloadURL = VertopalConnector.DOWNLOAD.getURL();
    // Retorna "https://api.vertopal.com/v1/download/file"
    String uploadURL = VertopalConnector.UPLOAD.getURL();
    // Retorna "https://api.vertopal.com/v1/upload/file"

    //TODO CODIGO COM A CONEXÃO VERTOPAL AQUI

}

