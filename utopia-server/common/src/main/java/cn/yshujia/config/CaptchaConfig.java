package cn.yshujia.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;


/**
 * @author yshujia
 * @create 2024/4/23
 * @description 验证码配置
 */
@Configuration
public class CaptchaConfig {
	
	@Bean(name = "captcha")
	public DefaultKaptcha getCaptchaBean() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		// 验证码配置
		Properties properties = createProperties(4);
		// CAPTCHA_SESSION_KEY
		properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");
		defaultKaptcha.setConfig(new Config(properties));
		return defaultKaptcha;
	}
	
	@Bean(name = "captchaMath")
	public DefaultKaptcha getCaptchaBeanMath() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		// 验证码配置
		Properties properties = createProperties(6);
		// CAPTCHA_SESSION_KEY
		properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
		// 验证码文本生成器
		properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "cn.yshujia.config.CaptchaCreator");
		// 验证码噪点颜色 默认为 Color.BLACK
		properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
		// 干扰实现类
		properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
		defaultKaptcha.setConfig(new Config(properties));
		return defaultKaptcha;
	}
	
	private Properties createProperties(Integer length) {
		Properties properties = new Properties();
		// 是否有边框 默认为 true 我们可以自己设置 yes，no
		properties.setProperty(KAPTCHA_BORDER, "yes");
		// 验证码文本字符颜色 默认为 Color.BLACK
		properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
		// 验证码图片宽度 默认为 200
		properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
		// 验证码图片高度 默认为 50
		properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
		// 验证码文本字符大小 默认为 40
		properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "38");
		// 验证码文本字符长度 默认为 5
		properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, length.toString());
		// 验证码文本字体样式 默认为 new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
		properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
		// 验证码文本字符间距 默认为 2
		properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
		// 图片样式 水纹 com.google.code.kaptcha.impl.WaterRipple
		// 鱼眼 com.google.code.kaptcha.impl.FishEyeGimpy
		// 阴影 com.google.code.kaptcha.impl.ShadowGimpy
		properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
		return properties;
	}
	
}
