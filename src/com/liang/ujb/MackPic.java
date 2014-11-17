package com.liang.ujb;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MackPic extends HttpServlet
{	private String savePath;
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
  }
	public void init(ServletConfig config) throws ServletException {
		savePath=config.getServletContext().getRealPath("/")+"pics\\";
		File tmp_path=new File(savePath);
		tmp_path.mkdirs();

		System.out.println("照片数据保存路径:"+savePath);
	}

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String width = request.getParameter("width");
    String height = request.getParameter("height");
    int w = Integer.parseInt(width);
    int h = Integer.parseInt(height);
    try {
    
      BufferedImage bf = new BufferedImage(w, h, 1);

      for (int i = 0; i < bf.getHeight(); i++) {
        String data = request.getParameter("px" + i);
        
        String[] ds = data.split(",");
        for (int j = 0; j < bf.getWidth(); j++) {
          int d = Integer.parseInt(ds[j], 16);
          bf.setRGB(j, i, d);
        }
      }

      ImageWriter writer = null;
      ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(bf);
      Iterator iter = ImageIO.getImageWriters(type, "jpg");
      if (iter.hasNext()) {
        writer = (ImageWriter)iter.next();
      }
      IIOImage iioImage = new IIOImage(bf, null, null);
      ImageWriteParam param = writer.getDefaultWriteParam();
     // param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionMode(2);

      param.setCompressionQuality(0.2F);
		String filename=String.valueOf(System.currentTimeMillis())+".jpg";

      String file=this.savePath+filename;
      FileOutputStream fossss=new FileOutputStream(file);
      ImageOutputStream outputStream2 = 
        ImageIO.createImageOutputStream(fossss);
      writer.setOutput(outputStream2);
      writer.write(null, iioImage, param);
      outputStream2.close();
      fossss.close();
      response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		out.print("<img src='"+request.getContextPath()+"//pics//"+filename+"'/>");
		out.flush();
		out.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("w = " + w + ",h=" + h);
    
  
  }
}