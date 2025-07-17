package cn.yshujia.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class GeneratorUtils {
	
	private static final String AUTHOR = "YShuJia";
	
	private static final String DATE = "2024/4/23";
	
	private static final String PARENT_PACKAGE = "cn.yshujia";
	
	// 指定输出模块 service 表示模块名
	private static final String PARENT = "service/src/main/java/cn/yshujia";
	
	private static final String PARENT_XML = "service/src/main/resources/mapper";
	
	private static final String[] TABLE_PREFIX = new String[]{"t_"};
	
	// 包含的表 为空时 生成所有表
	private static final String[] INCLUDE_TABLE = new String[]{"t_develop"};
	
	private static final String USERNAME = "root";
	
	private static final String PASSWORD = "123456";
	
	private static final String URL = "jdbc:mysql://localhost:3306/utopia?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
	
	private static Map<OutputFile, String> getPathInfo() {
		
		String entity = PARENT + "/domain/entity";
		String mapper = PARENT + "/mapper";
		String service = PARENT + "/service";
		String serviceImpl = PARENT + "/service/impl";
		String controllerPath = PARENT + "/controller";
		
		Map<OutputFile, String> pathInfo = new HashMap<>(6);
		pathInfo.put(OutputFile.entity, entity);
		pathInfo.put(OutputFile.mapper, mapper);
		pathInfo.put(OutputFile.service, service);
		pathInfo.put(OutputFile.serviceImpl, serviceImpl);
		pathInfo.put(OutputFile.controller, controllerPath);
		pathInfo.put(OutputFile.xml, PARENT_XML);
		return pathInfo;
	}
	
	public static void generator() {
		FastAutoGenerator
				.create(URL, USERNAME, PASSWORD)
				
				.globalConfig(builder ->
						              builder
								              .enableSpringdoc()
								              .author(AUTHOR)
								              .dateType(DateType.ONLY_DATE)
								              .commentDate(DATE)
								              .disableOpenDir())
				
				.packageConfig(builder ->
						               builder.parent(PARENT_PACKAGE)
								               .pathInfo(getPathInfo()))
				
				.strategyConfig(builder -> {
					
					if (!ObjectUtils.isEmpty(INCLUDE_TABLE)) {
						builder.addInclude(INCLUDE_TABLE);
					}
					
					builder.enableCapitalMode()
							.disableSqlFilter()
							.addTablePrefix(TABLE_PREFIX)
							.entityBuilder()
							.enableFileOverride()
							.enableLombok()
							.enableChainModel()
							.enableRemoveIsPrefix()
							.enableTableFieldAnnotation()
							.versionColumnName("version")
							.versionPropertyName("version")
							.logicDeleteColumnName("status")
							.logicDeletePropertyName("status")
							.naming(NamingStrategy.underline_to_camel)
							.columnNaming(NamingStrategy.underline_to_camel)
							.idType(IdType.ASSIGN_ID)
							.formatFileName("%s")
							.addTableFills(new Column("create_time", FieldFill.INSERT))
							.addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
							.addTableFills(new Column("create_by", FieldFill.INSERT))
							.addTableFills(new Column("update_by", FieldFill.INSERT_UPDATE))
							.controllerBuilder()
							.enableFileOverride()
							.enableHyphenStyle()
							.enableRestStyle()
							.formatFileName("%sController")
							.serviceBuilder()
							.enableFileOverride()
							.formatServiceFileName("%sService")
							.formatServiceImplFileName("%sServiceImpl")
							.mapperBuilder()
							.enableFileOverride()
							.enableBaseColumnList()
							.enableBaseResultMap()
							.mapperAnnotation(Mapper.class)
							.formatMapperFileName("%sMapper")
							.formatXmlFileName("%sMapper");
				})
				.templateEngine(new FreemarkerTemplateEngine())
				.execute();
	}
	
	public static void main(String[] args) {
		generator();
	}
	
}
