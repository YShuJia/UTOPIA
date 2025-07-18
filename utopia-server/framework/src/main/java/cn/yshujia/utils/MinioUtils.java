package cn.yshujia.utils;

import cn.yshujia.config.MinioConfig;
import cn.yshujia.enums.MinioFolder;
import cn.yshujia.ex.CustomException;
import com.luciad.imageio.webp.WebPWriteParam;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author yshujia
 * @create 2024/12/26
 * @description MinioUtils
 */

@Slf4j
@Component
public class MinioUtils {

	public static String STATIC_DOMAIN;

	private static MinioConfig config;

	private static MinioClient minioClient;

	@Autowired
	public MinioUtils(MinioConfig config, MinioClient minioClient) {
		MinioUtils.config = config;
		STATIC_DOMAIN = config.getDomain();
		MinioUtils.minioClient = minioClient;
	}

	public static String upload(MultipartFile file, MinioFolder folder) {
		return uploadNameUrl(file, folder).get(file.getOriginalFilename());
	}

	public static List<String> upload(MultipartFile[] files, MinioFolder folder) {
		List<String> urls = new ArrayList<>();
		for (MultipartFile file : files) {
			String url = upload(file, folder);
			urls.add(url);
		}
		return urls;
	}

	/**
	 * @author: yshujia
	 * @create: 2025/6/30 16:16
	 * @description: uploadNameUrl 返回原文件名 和 url
	 * @params: [file, folder] 传入文件和上传的文件夹
	 * @return: Map<String, String> key => 原文件名, value => url
	 */
	public static Map<String, String> uploadNameUrl(MultipartFile file, MinioFolder folder) {
		Boolean isImage = isImage(file);
		String uniqueFileName = createUrl(folder.getFolder(), isImage);
		String contentType = isImage ? "image/webp" : "video/mp4";
		byte[] bytes = isImage ? createWebp(file) : createMp4(file);
		uploadFile(bytes, uniqueFileName, contentType);
		// 创建 map
		Map<String, String> map = new HashMap<>();
		map.put(file.getOriginalFilename(), uniqueFileName);
		return map;
	}

	public static Map<String, String> uploadNameUrl(MultipartFile[] files, MinioFolder folder) {
		Map<String, String> urls = new HashMap<>();
		for (MultipartFile file : files) {
			Map<String, String> map = uploadNameUrl(file, folder);
			urls.putAll(map);
		}
		return urls;
	}

	/**
	 * @author: yshujia
	 * @create: 2025/6/30 16:17
	 * @description: uploadUrlKb 返回上传的 url 和 size (kb)
	 * @params: [file, folder] 传入文件和上传的文件夹
	 * @return: Map<String, BigDecimal> key => url, value => size (kb)
	 */
	public static Map<String, BigDecimal> uploadUrlKb(MultipartFile file, MinioFolder folder) {
		Boolean isImage = isImage(file);
		String uniqueFileName = createUrl(folder.getFolder(), isImage);
		String contentType = isImage ? "image/webp" : "video/mp4";
		byte[] bytes = isImage ? createWebp(file) : createMp4(file);
		uploadFile(bytes, uniqueFileName, contentType);
		// 返回map
		Map<String, BigDecimal> map = new HashMap<>();
		map.put(uniqueFileName, BigDecimal.valueOf(bytes.length / 1024L));
		return map;
	}

	public static Map<String, BigDecimal> uploadUrlKb(MultipartFile[] files, MinioFolder folder) {
		Map<String, BigDecimal> urls = new HashMap<>();
		for (MultipartFile file : files) {
			Map<String, BigDecimal> map = uploadUrlKb(file, folder);
			urls.putAll(map);
		}
		return urls;
	}

	public static void delete(Collection<String> paths) {
		if (!CollectionUtils.isEmpty(paths)) {
			for (String path : paths) {
				delete(path);
			}
		}
	}

	public static void delete(Map<String, ?> map) {
		if (!CollectionUtils.isEmpty(map)) {
			Set<String> list = map.keySet();
			for (String path : list) {
				delete(path);
			}
		}
	}

	public static void delete(String path) {
		if (StringUtils.isEmpty(path)) {
			return;
		}
		path = path.replaceAll(MinioUtils.STATIC_DOMAIN, "");
		RemoveObjectArgs args = RemoveObjectArgs.builder().bucket(config.getBucketName()).object(path).build();
		try {
			minioClient.removeObject(args);
			log.info("文件删除成功: {}", path);
		} catch (Exception e) {
			log.error("文件删除错误: {}", e.getLocalizedMessage());
		}
	}

	private static String createUrl(String folder, Boolean isImage) {
		if (isImage) {
			return folder + "/" + TimeUtils.getParallelDate() + "/" + TimeUtils.getParallelTime() + ".webp";
		} else {
			return folder + "/" + TimeUtils.getParallelDate() + "/" + TimeUtils.getParallelTime() + ".mp4";
		}
	}

	private static void uploadFile(byte[] bytes, String uniqueFileName, String contentType) {
		// 检查配置和文件名
		String bucketName = config.getBucketName();
		if (bucketName == null || bucketName.isEmpty()) {
			throw new CustomException("找不到 Minio 存储桶！");
		}
		InputStream inputStream = new ByteArrayInputStream(bytes);
		try {
			PutObjectArgs args = PutObjectArgs.builder()
					.bucket(bucketName)
					.object(uniqueFileName)
					.stream(inputStream, bytes.length, -1)
					.contentType(contentType)
					.build();
			minioClient.putObject(args);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	private static Boolean isImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new CustomException("上传的文件不能为空！");
		}
		String contentType = file.getContentType();
		return contentType != null && contentType.startsWith("image");
	}

	private static byte[] createWebp(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new CustomException("上传的图片不能为空！");
		}
		try (InputStream fis = file.getInputStream(); ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
			BufferedImage image = ImageIO.read(fis);
			if (image == null) {
				throw new CustomException("该文件不是图片！");
			}
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");
			if (!writers.hasNext()) {
				throw new CustomException("找不到 webp 的输入流！");
			}
			ImageWriter writer = writers.next();
			WebPWriteParam writeParam = getWebPWriteParam(file, writer);
			try (ImageOutputStream ios = new MemoryCacheImageOutputStream(bao)) {
				writer.setOutput(ios);
				writer.write(null, new IIOImage(image, null, null), writeParam);
			} finally {
				// 确保所有资源被释放
				writer.dispose();
			}
			// 将字节数组转换为 InputStream 并返回
			return bao.toByteArray();
		} catch (IOException e) {
			throw new CustomException(e.getMessage());
		}
	}

	private static WebPWriteParam getWebPWriteParam(MultipartFile file, ImageWriter writer) {
		WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
		writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		boolean isWebp = Objects.requireNonNull(file.getContentType()).contains("webp");
		// 如果不是 webp 格式 并且超过大小 则使用有损压缩
		if (!isWebp && file.getSize() >= config.getMaxImgSize()) {
			// 设置有损压缩
			writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
			// 设置压缩质量. 范围 0-1
			writeParam.setCompressionQuality(config.getCompressionQuality());
		} else {
			// 默认无损压缩
			writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSLESS_COMPRESSION]);
		}
		return writeParam;
	}

	/**
	 * @author: yshujia
	 * @create: 2025/7/15 21:36
	 * @description: createMp4 将上传的视频文件转为 mp4 格式，返回 byte[]
	 * @params: [file] file 上传的 MultipartFile 视频文件
	 * @return: byte 转换后的 mp4 字节流
	 */
	public static byte[] createMp4(MultipartFile file) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(file.getBytes());
			return baos.toByteArray();
		} catch (IOException e) {
			throw new CustomException(e.getMessage());
		}
	}
}
