package util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Project Name:watermark
 * File Name:WatermarkUtil
 * Package Name:util
 * Date:2019/4/15
 * Author:zengxueshan
 * Description:
 * Copyright (c) 2019, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class WatermarkUtil {
    private static final String FONT_NAME = "宋体";
    private static final int FONT_STYLE = 0;
    private static final int FONT_SIZE = 12;
    private static final Color FONT_COLOR = Color.BLACK;
    private static final String MARK_TEXT = "你好，世界";
    private static final float ALPHA = 0.2f;
    private static final String UPLOAD_PATH = "D:\\图片\\";

    public static void main(String[] args) {
        textWaterMark(new File("D:\\图片\\桌面壁纸四象限.png"),"桌面壁纸四象限1.png");
    }
    //计算水印文本长度
    //1、中文长度即文本长度 2、英文长度为文本长度二分之一
    public static int getTextLength(String text){
        //水印文字长度
        int length = text.length();

        for (int i = 0 ; i < text.length(); i++) {
            String s =String.valueOf(text.charAt(i));
            if (s.getBytes().length>1) {
                length++;
            }
        }
        length = length%2==0?length/2:length/2+1;
        return length;
    }
    //添加单条文字水印方法
    public static String textWaterMark(File myFile,String imageFileName) {
        InputStream is =null;
        OutputStream os =null;
        int X = 636;
        int Y = 700;

        try {
            //使用ImageIO解码图片
            Image image = ImageIO.read(myFile);
            //计算原始图片宽度长度
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            //创建图片缓存对象
            BufferedImage bufferedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
            //创建java绘图工具对象
            Graphics2D graphics2d = bufferedImage.createGraphics();
            //参数主要是，原图，坐标，宽高
            graphics2d.drawImage(image, 0, 0, width, height, null);
            graphics2d.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
            graphics2d.setColor(FONT_COLOR);

            //使用绘图工具将水印绘制到图片上
            //计算文字水印宽高值
            int waterWidth = FONT_SIZE*getTextLength(MARK_TEXT);
            int waterHeight = FONT_SIZE;
            //计算水印与原图高宽差
            int widthDiff = width-waterWidth;
            int heightDiff = height-waterHeight;
            //水印坐标设置
            if (X > widthDiff) {
                X = widthDiff;
            }
            if (Y > heightDiff) {
                Y = heightDiff;
            }
            //水印透明设置
            graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
            //纵坐标在下方，不增加字体高度会靠上
            graphics2d.drawString(MARK_TEXT, X, Y+FONT_SIZE);

            graphics2d.dispose();
            os = new FileOutputStream(UPLOAD_PATH+"\\"+imageFileName);
            //创建图像编码工具类
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
            //使用图像编码工具类，输出缓存图像到目标文件
            en.encode(bufferedImage);
            if(is!=null){
                is.close();
            }
            if(os!=null){
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
