package org.chatapp.io;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.io.*;

@Component
public class FileWriter implements Writer{

    private final String imageFolder = "images";
    private final Environment environment;
    private final String filePath;

    @Autowired
    public FileWriter(Environment environment) {
        this.environment = environment;
        this.filePath = this.environment.getProperty("PUBLIC");
    }


    @Override
    public String uploadFile(byte[] image, String email, String fileType) {

        String path = this.filePath + "\\" + this.imageFolder + "\\";
        String imageUrl = email + ".";
        String type = "";
        if(fileType.indexOf("/") != -1) {
            type = fileType.split("/")[1];
        }

        File file = new File(path);
        try (FileImageOutputStream fos = new FileImageOutputStream(new File(imageUrl + type))) {
            fos.write(image,0, image.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageUrl + type;
    }

}
