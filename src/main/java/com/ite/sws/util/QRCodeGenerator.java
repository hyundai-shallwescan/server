package com.ite.sws.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * QR 코드 생성
 * @author 김민정
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	김민정       최초 생성
 * 2024.08.28  	김민정       QR 코드를 생성하여 File 형식으로 반환하는 메서드
 * 2024.08.28  	김민정       QR 코드를 생성하여 byte[] 형식으로 반환하는 메서드
 * 2024.08.28  	김민정       byte[]을 File로 변환하는 메서드
 * </pre>
 */
@Component
public class QRCodeGenerator {

    // QR 코드를 생성하여 File 형식으로 반환하는 메서드
    public static File generateQRCodeFile(String qrText,String fileName, int width, int height)
            throws WriterException, IOException {
        byte[] qrCodeImage = generateQRCodeImage(qrText, width, height);
        return convertByteArrayToFile(qrCodeImage, "qrcode-" + fileName);
    }

    // QR 코드를 생성하여 byte[] 형식으로 반환하는 메서드
    public static byte[] generateQRCodeImage(String qrText, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    // byte[]을 File로 변환하는 메서드
    private static File convertByteArrayToFile(byte[] byteArray, String fileName) throws IOException {
        File file = File.createTempFile(fileName, ".png");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(byteArray);
        }
        return file;
    }
}
