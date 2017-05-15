package com.ecochain.ledger.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImgUtil {
	/**
	 * 计算图片尺寸大小等信息：w宽、h高、s大小。异常时返回null。
	 * 
	 * @param imgpath
	 *            图片路径
	 * @return 图片信息map
	 */
	public static Map<String, Long> getImgInfo(String imgpath) {
		Map<String, Long> map = new HashMap<String, Long>(3);
		File imgfile = new File(imgpath);
		try {
			FileInputStream fis = new FileInputStream(imgfile);
			BufferedImage buff = ImageIO.read(imgfile);
			map.put("w", buff.getWidth() * 1L);
			map.put("h", buff.getHeight() * 1L);
			map.put("s", imgfile.length());
			fis.close();
		} catch (FileNotFoundException e) {
			System.err.println("所给的图片文件" + imgfile.getPath()
					+ "不存在！计算图片尺寸大小信息失败！");
			map = null;
		} catch (IOException e) {
			System.err.println("计算图片" + imgfile.getPath() + "尺寸大小信息失败！");
			map = null;
		}
		return map;
	}
	/**
	 * @param x1 目标切片起点x坐标
	 * @param y1 目标切片起点y坐标
	 * @param width 目标切片宽度
	 * @param height  目标切片高度
	 * @param sourcePath 源图片地址
	 * @param outImgUrl 输出图片路径
	 */
	public static void cut(int x1, int y1, int width, int height,String sourcePath,String outImgUrl) {
		FileInputStream is = null;
		ImageInputStream iis = null;
		try {
			is = new FileInputStream(sourcePath.trim());
			String fileSuffix = sourcePath.substring(sourcePath.trim().lastIndexOf(".") + 1);
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(fileSuffix.trim());
			ImageReader reader = it.next();
			iis = ImageIO.createImageInputStream(is);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x1, y1, width, height);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			File file = new File(outImgUrl);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			ImageIO.write(bi, fileSuffix.trim(), file);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is = null;
			}
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				iis = null;
			}
		}
	}
}
