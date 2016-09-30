package com.ruimin.ifinflow.model.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtil {
	private static Log log = LogFactory.getLog(FileUtil.class);

	public static void string2File(String content, String filePath) {
		FileWriter writer = null;
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();

			URL fileUrl = classLoader.getResource(filePath);

			writer = new FileWriter(fileUrl.getFile());
			writer.write(content);
			return;
		} catch (IOException e) {
			log.error("将字符串写入文件发生异常！", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String file2String(String filePath, String charset) {
		StringBuffer sb = new StringBuffer();
		LineNumberReader reader = null;
		InputStream is = null;
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();

			URL url = classLoader.getResource(filePath);

			is = new FileInputStream(url.getFile());
			reader = new LineNumberReader(new BufferedReader(
					new InputStreamReader(is)));

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append(System.getProperty("line.separator"));
			}

			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			log.error("读取文件为一个内存字符串失败，失败原因是使用了不支持的字符编码" + charset, e);
		} catch (FileNotFoundException e) {
			log.error("读取文件为一个内存字符串失败，失败原因所给的文件" + filePath + "不存在！", e);
		} catch (IOException e) {
			log.error("读取文件为一个内存字符串失败，失败原因是读取文件异常！", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("关闭LineNumberReader异常！", e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error("关闭LineNumberReader异常！", e);
				}
			}
		}
		return null;
	}
}