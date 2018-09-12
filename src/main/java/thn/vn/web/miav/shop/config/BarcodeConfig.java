package thn.vn.web.miav.shop.config;

import org.krysalis.barcode4j.tools.UnitConv;

public class BarcodeConfig {
    public static class Barcode {
        private int dpi;

        private double height;

        private double barHeight;

        private double fontSize;

        private double moduleWidth;

        public int getDpi() {
            return dpi;
        }

        public void setDpi(int dpi) {
            this.dpi = dpi;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public double getBarHeight() {
            return barHeight;
        }

        public void setBarHeight(double barHeight) {
            this.barHeight = barHeight;
        }

        public double getFontSize() {
            return fontSize;
        }

        public void setFontSize(double fontSize) {
            this.fontSize = fontSize;
        }

        public double getModuleWidth() {
            return moduleWidth;
        }

        public void setModuleWidth(double moduleWidth) {
            this.moduleWidth = moduleWidth;
        }

        public static BarcodeConfig.Barcode defaultConfig() {
            BarcodeConfig.Barcode barcodeConfig = new BarcodeConfig.Barcode();
            barcodeConfig.setDpi(100);
            barcodeConfig.setBarHeight(40);
            barcodeConfig.setHeight(64);
            barcodeConfig.setFontSize(18.0f);
            barcodeConfig.setModuleWidth(UnitConv.in2mm(4.0f / barcodeConfig.getDpi()));
            return barcodeConfig;
        }
    }
}
