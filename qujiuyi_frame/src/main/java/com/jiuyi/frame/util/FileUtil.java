package com.jiuyi.frame.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import com.jiuyi.frame.conf.DBConfigStatic;
import com.jiuyi.frame.helper.Loggers;

/**
 * @Author: xutaoyang @Date: 上午10:06:46
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class FileUtil {
	
	private static final String DEFAULT_ENCODING = "UTF-8";
	public static String writeFile(String dir, String fileName, String suffix, MultipartFile file) {
		return writeFile(dir, fileName + "." + suffix, file);
	}

	/** 磁盘路径 */
	public static String getServerFileDir() {
		return DBConfigStatic.getConfig("server.file.dir");
	}

	/** http开头的用于浏览器访问的路径 */
	public static String getServerFileUrl() {
		return DBConfigStatic.getConfig("server.file.url");
	}

	public static String writeFile(String format, MultipartFile file) {
		return writeFileFormat(getServerFileDir(), format, file);
	}

	public static String writeFileFormat(String dir, String format, MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return null;
		}
		String originFileName = file.getOriginalFilename();
		String suffix = getSuffix(file);
		originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
		String savePath = format + suffix;
		savePath = PathFormat.parse(savePath, originFileName);
		String physicalPath = dir + savePath;
		File diskFile = new File(physicalPath);
		try {
			if (!diskFile.exists()) {
				diskFile.getParentFile().mkdirs();// 如果已经存在则不会创建
				diskFile.createNewFile();
			}
			byte[] bytes = file.getBytes();
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(diskFile));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			Loggers.err("write file err", e);
			return null;
		}
		return savePath;
	}

	public static String writeFile(String dir, String fileName, MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				File diskFile = new File(dir, fileName);
				if (!diskFile.exists()) {
					diskFile.createNewFile();
				}
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(diskFile));
				stream.write(bytes);
				stream.close();
				return diskFile.getPath();
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getSuffix(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
	
	/**
	 * 写入文件内容
	 * 
	 * @param pathFormat
	 *            {filename} //会替换成文件名 [要注意中文文件乱码问题] 
	 *            {rand:6}  //会替换成随机数,后面的数字是随机数的位数 
	 *            {time} //会替换成时间戳 
	 *            {yyyy} //会替换成四位年份 
	 *            {yy} //会替换成两位年份 
	 *            {mm} //会替换成两位月份 
	 *            {dd} //会替换成两位日期 
	 *            {hh} //会替换成两位小时
	 *            {ii} //会替换成两位分钟 
	 *            {ss} //会替换成两位秒
	 * @param content 内容
	 */ 
	public static String writeStr(String pathFormat, String content) {
		String path = PathFormat.parse(pathFormat);
		try {
			FileUtils.write(new File(path), content, DEFAULT_ENCODING);
		} catch (IOException e) {
			Loggers.errf(e, "write file err,pathFormat:%s", pathFormat);
		}
		return path;
	}

	/**
	 * 读文件的全部内容
	 * 
	 * @param path
	 * @return
	 */
	public static String readStr(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		String res = null;
		try {
			res = FileUtils.readFileToString(file, DEFAULT_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
}
