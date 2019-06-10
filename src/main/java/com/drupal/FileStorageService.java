package com.drupal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class FileStorageService {
	private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            if(targetLocation.toFile().exists()) {
            	System.out.println("File already exists....overwriting");
            }
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String command = "bash -c ffmpeg -i "+fileName+" -frames:v 1 "+fileName.substring(0, fileName.lastIndexOf('.'))+".bmp";
            System.out.println(command);
            String fullLoc = targetLocation.toString();
            Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","ffmpeg -i "+fullLoc+" -frames:v 1 "+fullLoc.substring(0, fullLoc.lastIndexOf('.'))+".bmp"});
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }
    
//    public void extractImage() {
//    	FFmpegFrameGrabber g = new FFmpegFrameGrabber("C:\\JavaEE\\New Project\\tape\\src\\main\\webapp\\web-resources\\videos\\vid.mp4");
//        g.setFormat("mp4");
//        g.start();
//
//        for (int i = 0 ; i < 50 ; i++) {
//            ImageIO.write(g.grab()., "png", new File("C:\\JavaEE\\New Project\\tape\\src\\main\\webapp\\web-resources\\thumbnails\\video-frame-" + System.currentTimeMillis() + ".png"));
//        }
//
//         g.stop();
//
//    }
}
