package cn.yshujia.service;


import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author: yshujia
 * @create: 2025/8/28 20:32
 * @description: EmailService 邮件服务
 */


@Slf4j
@Service
public class EmailService {

	@Resource
	private JavaMailSenderImpl sender;

	@Resource
	private TemplateEngine templateEngine;

	public void captchaUpdate(String email, String code, Integer expireTimeOfMinute) {
		captcha(email, "本次修改信息", code, expireTimeOfMinute);
	}

	public void captchaRegister(String email, String code, Integer expireTimeOfMinute) {
		captcha(email, "感谢您注册成为UTOPIA博客网站的用户，您", code, expireTimeOfMinute);
	}

	public void captchaLogin(String email, String code, Integer expireTimeOfMinute) {
		captcha(email, "本次登录", code, expireTimeOfMinute);
	}

	@Async("Task")
	public void captcha(String email, String operate, String code, Integer expireTimeOfMinute) {
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("YShuJia <yshujia2001@163.com>");
			//设置接收邮件的人，可以多个
			helper.setTo(email);
			//设置发送邮件的主题
			helper.setSubject("验证码");
			//设置发送邮件的内容 第二个设置为true则可以发送带HTML的邮件
			helper.setText(createTemplate(operate, code, expireTimeOfMinute + "分钟"), true);
			sender.send(message);
		} catch (MessagingException e) {
			log.error("邮件发送失败：{}", e.getMessage());
		}
	}

	private String createTemplate(String operator, String code, String expireTime) {
		Context context = new Context();
		// 设置模板所需的参数
		context.setVariable("operate", operator);
		context.setVariable("code", code);
		context.setVariable("expireTime", expireTime);
		// 通过模板类将动态参数传入 HTML 模板，并返回模板内容
		return templateEngine.process("code-template", context);
	}
}
