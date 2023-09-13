package com.ruoyi.common;

import cn.hutool.core.img.Img;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;

import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Test {

    /**
     * 生成缩略图
     *
     * @param oidPath 文件大小
     * @param newPath 原文件路径
     * @param smallSize 文件压缩倍数
     * @return
     */
    public static boolean imageGenerateSmall(String oidPath, String newPath, double smallSize) {
        try {
            File bigFile = new File(oidPath);
            Image image = ImageIO.read(bigFile);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            int widthSmall = (int) (width / smallSize);
            int heightSmall = (int) (height / smallSize);
            BufferedImage buffi = new BufferedImage(widthSmall, heightSmall, BufferedImage.TYPE_INT_RGB);
            Graphics g = buffi.getGraphics();
            g.drawImage(image, 0, 0, widthSmall, heightSmall, null);
            g.dispose();
            ImageIO.write(buffi, "jpg", new File(newPath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 图片压缩，图片大小超过100K自动按比例压缩到100K以下
     *
     * @param fileSize 文件大小
     * @param oidPath 原文件路径
     * @param newPath 压缩后路径
     * @return
     * @throws Exception
     */
    public static boolean imageCompress(long fileSize, String oidPath, String newPath) throws Exception {
        boolean rs = true;
        int kb = 100 * 1024;
        if (fileSize > 100 * 1024) {
            int smallSize = (int) (fileSize % kb == 0 ? fileSize / kb : fileSize / kb + 1);
            double size = Math.ceil(Math.sqrt(smallSize));
            rs = imageGenerateSmall(oidPath, newPath, size);
        }
        return rs;
    }


    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\admin\\Desktop\\temp\\xxx.jpg");
        System.out.println("--------------文件大小（byte）----------------:"+file.length());
        imageCompress(file.length(), "C:\\Users\\admin\\Desktop\\temp\\xxx.jpg", "C:\\Users\\admin\\Desktop\\temp\\output.jpg");
    }
}
