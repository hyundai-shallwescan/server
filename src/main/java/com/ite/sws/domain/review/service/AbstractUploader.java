package com.ite.sws.domain.review.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractUploader<T> {

    protected final String bucketName;
    private final S3Service s3Service;

    protected AbstractUploader(S3Service s3Service, String bucketName) {
        this.s3Service = s3Service;
        this.bucketName = bucketName;
    }

    public void upload(T createRequest, String filePrefixName, List<MultipartFile> files) {
        List<String> fileIdentifiers = new ArrayList<>();
        List<File> convertedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                File convertedFile = convertMultipartFileToFile(file);
                String fileIdentifier = s3Service.getPreAssignedUrl(
                    filePrefixName + file.getOriginalFilename(), bucketName
                );
                fileIdentifiers.add(fileIdentifier);
                convertedFiles.add(convertedFile);
            }

            processDomain(createRequest, fileIdentifiers);

            for (int i = 0; i < files.size(); i++) {
                s3Service.uploadFile(
                    filePrefixName + files.get(i).getOriginalFilename(), bucketName,
                    convertedFiles.get(i)
                );
            }

        } catch (IOException e) {
            handleUploadException(e, files, filePrefixName);
        } catch (PersistenceException e) {
            cleanupOnFailure(files, filePrefixName);
            handlePersistenceException(e);
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    protected abstract void processDomain(T createRequest, List<String> fileIdentifiers);

    protected abstract void handlePersistenceException(PersistenceException e);

    protected abstract void handleGeneralException(Exception e);

    protected void handleUploadException(IOException e, List<MultipartFile> files,
        String filePrefixName) {
        cleanupOnFailure(files, filePrefixName);
        throw new AmazonS3Exception("S3 upload error", e);
    }

    protected void cleanupOnFailure(List<MultipartFile> files, String filePrefixName) {
        for (MultipartFile file : files) {
            s3Service.deleteFile(filePrefixName + file.getOriginalFilename(), bucketName);
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile(Objects.requireNonNull(file.getOriginalFilename()),
            null);
        file.transferTo(tempFile);
        return tempFile;
    }
}
