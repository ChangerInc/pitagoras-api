package changer.pitagoras.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class MultipartFileMock implements MultipartFile {

    private final ByteArrayInputStream inputStream;
    private final String filename;

    public MultipartFileMock(ByteArrayInputStream inputStream, String filename) {
        this.inputStream = inputStream;
        this.filename = filename;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return "image/png";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return inputStream.available();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(getBytes());
    }
}
