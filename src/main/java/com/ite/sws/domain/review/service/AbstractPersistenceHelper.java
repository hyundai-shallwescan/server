package com.ite.sws.domain.review.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.ite.sws.constant.UploadCommand;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
/**
 * 다양한 곳에 적용될 수 있는 미디어 파일 Upload Template abstract class
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	구지웅      최초 생성
 * 2024.08.26  	구지웅      MediaPersistLocationStrategy 의존성 추가
 * </pre>
 *
 */
public abstract class AbstractPersistenceHelper<T> {

    protected final String bucketName;
    private final MediaPersistLocationStrategy persistLocationStrategy;

    protected AbstractPersistenceHelper(MediaPersistLocationStrategy persistLocationStrategy, String bucketName) {
        this.persistLocationStrategy = persistLocationStrategy;
        this.bucketName = bucketName;
    }

    @Transactional
    public void upload(T createRequest, List<MultipartFile> files,
        UploadCommand uploadCommand) {
        List<String> fileIdentifiers = new ArrayList<>();
        List<File> convertedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                File convertedFile = convertMultipartFileToFile(file);
                String fileIdentifier = persistLocationStrategy.getPreAssignedUrl(
                    file.getOriginalFilename(), bucketName
                );
                fileIdentifiers.add(fileIdentifier);
                convertedFiles.add(convertedFile);
            }


            processDomain(createRequest, fileIdentifiers, uploadCommand);

            for (int i = 0; i < files.size(); i++) {
                persistLocationStrategy.save(
                    files.get(i).getOriginalFilename(), bucketName,
                    convertedFiles.get(i)
                );
            }

        } catch (AmazonS3Exception e) {
            handleUploadException(files);
        } catch (PersistenceException e) {
            cleanupOnFailure(files);
            handlePersistenceException();
        } catch (Exception e) {
            handleGeneralException();
        }
    }

    protected abstract void processDomain(T createRequest, List<String> fileIdentifiers,
        UploadCommand uploadCommand);

    protected void handlePersistenceException() {
        throw new CustomException(ErrorCode.DATABASE_ERROR);
    }


    protected void handleGeneralException() {
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    protected void handleUploadException(List<MultipartFile> files) {
        cleanupOnFailure(files);
        throw new CustomException(ErrorCode.PERSIST_EXCEPTION_TO_THIRD_PARTY);
    }

    protected void cleanupOnFailure(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            persistLocationStrategy.remove( file.getOriginalFilename(), bucketName);
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile(Objects.requireNonNull(file.getOriginalFilename()),
            null);
        file.transferTo(tempFile);
        return tempFile;
    }
}
