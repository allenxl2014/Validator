package com.liang.ujb;

import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.j2ee.web.WEBApplicationContext;
import org.fto.jthink.j2ee.web.fileload.File;
import org.fto.jthink.j2ee.web.fileload.FileUpload;

import com.liang.util.JBeanApplication;
import com.liang.util.Operator;
import com.liang.util.ValidatorConfiguration;

/**
 * 通用的文档上载Bean组件, 实现文档上传并写入相应的文档夹,同时写入数据库
 * 
 * 
 */
public class UploaderUJBean extends JBeanApplication {
	private FileUpload fileUpload = null;
	private Operator operator = null;

	public UploaderUJBean() {
	}

	public UploaderUJBean(HttpServletRequest req) throws Exception {
		initialize(req);
	}

	public void initialize(HttpServletRequest req) throws Exception {
		super.initialize(req);
		operator = (Operator) resManager.getResource(HttpSession.class
				.getName(), Operator.class.getName());
	}

	/**
	 * 上传文件
	 */
	public void upload() {
		if (fileUpload == null) {
			fileUpload = request.getFileUpload();
			fileUpload.upload();
		}
	}

	/**
	 * 关闭文件上传
	 */
	public void close() {
		if (fileUpload == null) {
			fileUpload.close();
		}
	}

	/**
	 * 上传文件
	 * 
	 */
	public String getFile() throws Exception {
		this.upload();
		String smallPic = null;
		if (fileUpload == null) {
			return "";
		}

		Iterator filesIT = fileUpload.getFiles().iterator();
		ValidatorConfiguration configx = (ValidatorConfiguration) resManager.getResource(WEBApplicationContext.class.getName(), ValidatorConfiguration.class.getName());
		String docPath = configx.getConfig().getChild("Document").getChildTextTrim("Root");
		while (filesIT.hasNext()) {
			File file = (File) filesIT.next();

			java.io.File fi = new java.io.File(docPath + "face"
					+ java.io.File.separator + operator.getSchoolId() + java.io.File.separator
					+ "temp" + java.io.File.separator);
			if (!fi.exists()) {
				boolean mkdir = fi.mkdirs();
			}

			String fileName = file.getFileName();
			if (fileName != null && fileName.trim().length() > 0 && file.getSize() > 0) {
				try {

					String imgAddr = "face" + java.io.File.separator
							+ operator.getSchoolId() + java.io.File.separator
							+ "temp" + java.io.File.separator
							+ new Date().getTime() + "." + file.getFileExt();

					System.out.println(docPath + imgAddr);
					file.saveAs(docPath + imgAddr);

					// 压缩图片
					smallPic = new SmallPicMaker(resManager).make(imgAddr);

				} catch (Exception e) {
					throw e;
				}
			}
		}
		this.close();
		return smallPic.replaceAll("\\\\", "/");
	}

}
