import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;

/**
 * @author: hellodk
 * @description CompressImageAndPaste
 * @date: 11/3/2023 1:13 PM
 */

public class CompressImageAndPaste {

    /**
     * 压缩倍率
     */
    private static final double compress_ratio = 0.4;

    public static void main(String[] args) {
        CompressImageAndPaste ciap = new CompressImageAndPaste();
        BufferedImage srcImage = ciap.readImageFromClipboard();
        if (srcImage == null) {
            return;
        }
        long sizeInBytes = ciap.getImageSizeInBytes(srcImage);
        String sizeInHumanReadable = ciap.formatSize(sizeInBytes);
        System.out.println("Original image size is " + sizeInHumanReadable + ".");
        BufferedImage compressedImage = ciap.compressImage(srcImage, compress_ratio); // 压缩比例设置为 0.4
        ciap.writeImage2Clipboard(compressedImage);
        long newSizeInBytes = ciap.getImageSizeInBytes(compressedImage);
        String newSizeInHumanReadable = ciap.formatSize(newSizeInBytes);
        System.out.print("Compressed image size is " + newSizeInHumanReadable);
        System.out.println(", and has been written to your clipboard, you can paste it anywhere.");
    }

    private BufferedImage readImageFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                return (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
            }
            catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Make sure your clipboard latest item is image and continue.");
            System.out.println("Jar usage description: read your clipboard image and compress it, finally write the compressed image to your clipboard, you can paste it anywhere.");
        }
        return null;
    }

    private BufferedImage compressImage(BufferedImage image, double compressRatio) {
        BufferedImage resImage = null;
        try {
            Path tmpFilePath = Files.createTempFile("compressed_image", ".jpg");
            File targetFile = tmpFilePath.toFile();
            Thumbnails.of(image)
                    .scale(1)
                    .outputQuality(compressRatio)
                    .toFile(targetFile);
            resImage = ImageIO.read(targetFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return resImage;
    }

    private void writeImage2Clipboard(BufferedImage image) {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        ImageTransferable it = new ImageTransferable(image);
        cb.setContents(it, null);
    }

    private long getImageSizeInBytes(BufferedImage image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", outputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        byte[] compressedImageBytes = outputStream.toByteArray();
        long sizeInBytes = compressedImageBytes.length;
        return sizeInBytes;
    }

    private String formatSize(long sizeInBytes) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = sizeInBytes;
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(size) + " " + units[unitIndex];
    }

    // 自定义Transferable类，用于将BufferedImage对象传输到剪贴板
    private static class ImageTransferable implements Transferable {
        private final BufferedImage image;

        public ImageTransferable(BufferedImage image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.imageFlavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.equals(DataFlavor.imageFlavor)) {
                return image;
            }
            else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }
}
