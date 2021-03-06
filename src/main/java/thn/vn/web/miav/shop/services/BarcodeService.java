package thn.vn.web.miav.shop.services;

import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.TextAlignment;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.stereotype.Service;
import thn.vn.web.miav.shop.config.BarcodeConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class BarcodeService {
    public byte[] barcode(String barcode, BarcodeConfig.Barcode barcodeConfig) throws IOException {
//        barcode = "0123456789";
        AbstractBarcodeBean codeBean = new Code39Bean();

        final int dpi = 160;
        codeBean.doQuietZone(false);
//        codeBean.setHeight(barcodeConfig.getHeight());
//        codeBean.setBarHeight(40.0);
//        codeBean.setFontSize(10.0);
        codeBean.setModuleWidth(barcodeConfig.getModuleWidth());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        BitmapCanvasProvider canvasProvider = new BitmapCanvasProvider(byteArrayOutputStream, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
//        canvasProvider.deviceCenteredText("hello",0.0,0,0.0,"Helvetica Neue",8);

        codeBean.generateBarcode(canvasProvider, barcode);

        canvasProvider.finish();

        return byteArrayOutputStream.toByteArray();
    }

    public BufferedImage barcode(String barcode) throws IOException {
        byte[] bytes = barcode(barcode, BarcodeConfig.Barcode.defaultConfig());

        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            // ignored
        }

        return null;
    }

    public String barcodeAsBase64(String code) throws IOException, InterruptedException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        BufferedImage bufferedImage = barcode(code);

        ImageIO.write(bufferedImage, "png", Base64.getEncoder().wrap(os));
        return os.toString(StandardCharsets.UTF_8.name());
    }

}
