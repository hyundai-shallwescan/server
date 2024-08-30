package com.ite.sws.domain.payment.service;

import com.ite.sws.constant.UploadCommand;
import com.ite.sws.domain.review.service.AbstractPersistenceHelper;
import com.ite.sws.domain.review.service.MediaPersistLocationStrategy;
import com.ite.sws.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import com.ite.sws.util.QRCodeGenerator;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

import static com.ite.sws.exception.ErrorCode.QR_GENERATE_ERROR;

/**
 * QR 코드 이미지를 생성하고 S3에 업로드하는 헬퍼 클래스
 * @author 김민정
 * @since 2024.08.30
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.30  	김민정       최초 생성
 * </pre>
 */
@Component
public class QRCodePersistenceHelper extends AbstractPersistenceHelper<String> {

    private final QRCodeGenerator qrCodeGenerator;

    public QRCodePersistenceHelper(MediaPersistLocationStrategy persistLocationStrategy,
                                   @Value("${cloud.aws.s3.exit_credential.bucket}") String bucketName,
                                   QRCodeGenerator qrCodeGenerator) {
        super(persistLocationStrategy, bucketName);
        this.qrCodeGenerator = qrCodeGenerator;
    }

    public String uploadQRCode(String qrText, String fileName) {
        try {
            // QR 코드 생성
            File qrFile = qrCodeGenerator.generateQRCodeFile(qrText, fileName, 300, 300);
            // S3에 업로드하고 URI 반환
            return upload(qrFile);
        } catch (Exception e) {
            throw new CustomException(QR_GENERATE_ERROR);
        }
    }

    @Override
    protected void processDomain(String createRequest, List<String> fileIdentifiers, UploadCommand uploadCommand) { }
}