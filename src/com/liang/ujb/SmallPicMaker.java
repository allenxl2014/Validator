package com.liang.ujb;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;

import org.apache.xerces.utils.regex.BMPattern;
import org.fto.jthink.exception.JThinkException;
import org.fto.jthink.j2ee.web.WEBApplicationContext;
import org.fto.jthink.resource.ResourceManager;

import com.liang.util.ValidatorConfiguration;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author Administrator
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class SmallPicMaker {

	int w = 120;
	int h = 130;

	ResourceManager resManager;
	String docPath = null;

	public SmallPicMaker(ResourceManager resManager) {
		this.resManager = resManager;
		ValidatorConfiguration configx = (ValidatorConfiguration) resManager.getResource(
				WEBApplicationContext.class.getName(), ValidatorConfiguration.class
						.getName());
		docPath = configx.getConfig().getChild("Document").getChildTextTrim(
				"Root");

	}

	/**
	 * 返回文件扩展名称
	 */
	private String getFileExt(String fileName) {
		String value = new String();
		int start = 0;
		int end = 0;
		if (fileName == null)
			return null;
		start = fileName.lastIndexOf('.') + 1;
		end = fileName.length();
		value = fileName.substring(start, end);
		if (fileName.lastIndexOf('.') >= 0)
			return value;
		else
			return "";
	}

	public String make(String srcPicFile) throws JThinkException {
		try {
			Class.forName("javax.imageio.ImageIO");
		} catch (Exception e) {
			return null;
		}

		java.io.File upFile = new java.io.File(docPath + srcPicFile); // 读入刚才上传的文件

		String extName = getFileExt(srcPicFile).trim();
		String newPicPath = srcPicFile.replace("temp" + java.io.File.separator, "");

		String newurl = docPath + newPicPath; // 新的缩略图保存地址
		FileOutputStream newimage = null;
		try {
			Image src;
			if (extName.equalsIgnoreCase("png")
					|| extName.equalsIgnoreCase("gif")
					|| extName.equalsIgnoreCase("jpeg")
					|| extName.equalsIgnoreCase("jpg")
					|| extName.equalsIgnoreCase("wmf")
					|| extName.equalsIgnoreCase("bmp")) {
				src = ImageIO.read(upFile); // 构造Image对象
			} else {
				throw new JThinkException("不支持此图像文件类型！");
			}

			BufferedImage tag = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D grap = tag.createGraphics();
			grap.setColor(Color.white);

			int x = 0;
			int y = 0;
			grap.drawImage(src, x, y, w, h, null); // 绘制缩小后的图
			grap.dispose();

			newimage = new FileOutputStream(newurl); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
			encoder.encode(tag); // 近JPEG编码
			// this.pressImage(docPath+"SmallPic"+java.io.File.separator+flagImg,
			// newurl, 0.8F);

		} catch (JThinkException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// throw new JThinkException("生成缩略图失败！");
		} finally {
			if (newimage != null) {
				try {
					newimage.close();
				} catch (Exception e) {
				}
			}
		}
		return newPicPath;
	}

	/** */
	/**
	 * 把图片印刷到图片上
	 * @param pressImg --
	 *            水印文件
	 * @param targetImg --
	 *            目标文件
	 * @param x
	 *            --x坐标
	 * @param y
	 *            --y坐标
	 * @param alpha
	 *            --透明度
	 */
	public final static void pressImage(String pressImg, String targetImg,
			float alpha) {
		try {
			// 目标文件
			float Alpha = alpha;
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			File _filebiao = new File(pressImg);
			Image src_biao = ImageIO.read(_filebiao);
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					Alpha));
			g.drawImage(src_biao, wideth - wideth_biao - 30, height
					- height_biao - 50, wideth_biao, height_biao, null);
			// 水印文件结束
			g.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回实际缩略图的宽度高度
	 */
	public int[] getSmallPicActualSize(Image src) {
		// float tagsize=270;
		int old_w = src.getWidth(null);
		int old_h = src.getHeight(null);
		int new_w = 0;
		int new_h = 0;
		if (old_w > old_h) {// 更宽
			if (old_w > 0 && old_h > 0) {
				if (old_w / old_h >= w / h) {
					if (old_w > w) {
						new_w = w;
						new_h = (old_h * w) / old_w;
					} else {
						new_w = old_w;
						new_h = old_h;
					}
				} else {
					if (old_h > h) {
						new_h = h;
						new_w = (old_w * h) / old_h;
					} else {
						new_w = old_w;
						new_h = old_h;
					}
				}
			}
		} else {// 更高
			if (old_w > 0 && old_h > 0) {
				if (old_h / old_w >= h / w) {
					if (old_h > h) {
						new_h = h;
						new_w = (old_w * h) / old_h;
					} else {
						new_h = old_h;
						new_w = old_w;
					}
				} else {
					if (old_w > w) {
						new_w = w;
						new_h = (old_h * w) / old_w;
					} else {
						new_h = old_h;
						new_w = old_w;
					}
				}
			}
		}
		return new int[] { new_w, new_h };
	}

}
