package com.drupal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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
	
	
	/**
	 * Stores the location of the directory in which the uploaded questions are stored.
	 */
	private final Path fileStorageLocation;
	/**
	 * Stores the location of the directory in which profile pictures are stored.
	 */
	private final Path profilePicPath;
	/**
	 * Stores the location of the directory where the answer videos are stored. 
	 */
	private final Path answersPath;
	
    /**
     * Constructor 
     * <p>
     * Accepts the FileStorageProperties as argument
     * and initializes a new FileStorageService object.
     * 
     * @param fileStorageProperties Keeps information as to in which directory a particular file is saved.
     */
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        String profilePicDir = fileStorageProperties.getProfilePicDir();
        System.out.println(profilePicDir);
        this.profilePicPath = Paths.get(profilePicDir).toAbsolutePath().normalize();
        this.answersPath = Paths.get(fileStorageProperties.getAnswersDir()).toAbsolutePath().normalize();
        try {
			Files.createDirectories(profilePicPath);
			Files.createDirectories(answersPath);
		} catch (IOException e) {
			System.out.println("could not create profile pic dir");
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
		}

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Stores file in the correct directory. 
     * @param file File to be stored in the appropriate directory.
     * @return Description of what error may have occurred while storing the file or a success message if no error.
     */
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
            System.out.println(targetLocation.toString());
            if(targetLocation.toFile().exists()) {
            	System.out.println("File already exists....overwriting");
            }
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Loads the file passed in as argument into a Resource object. 
     * @param fileName The name of the file to be loaded as the Resource object.
     * @param fileType The type of the file to be loaded as the Resource object.
     * @return The resource associated with the file name.
     */
    public Resource loadFileAsResource(String fileName, String fileType) {
        try {
            Path filePath;
            if(fileType.equals("image")){
            	filePath = this.profilePicPath.resolve(fileName).normalize();
            }
            else if(fileType.equals("answer")) {
            	filePath = this.answersPath.resolve(fileName).normalize();
            }
            else{
            	filePath = this.fileStorageLocation.resolve(fileName).normalize();
            }
            
            System.out.println(filePath.toString());
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

}
