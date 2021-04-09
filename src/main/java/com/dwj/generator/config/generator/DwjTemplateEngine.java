package com.dwj.generator.config.generator;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.dwj.generator.common.utils.ConvertUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Freemarker 模板引擎实现文件输出
 *
 * @author nieqiurong
 * @since 2018-01-11
 */
public class DwjTemplateEngine extends AbstractTemplateEngine {

    protected static final Logger logger = LoggerFactory.getLogger(DwjTemplateEngine.class);

    private Configuration configuration;

    @Override
    public DwjTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(ConstVal.UTF8);
        //生成临时模板文件
        DwjInjectionConfig injectionConfig = (DwjInjectionConfig) configBuilder.getInjectionConfig();
        File tempDirectory = (File)injectionConfig.getMap().get("temp_directory");
        OutputStream outputStream = null;
        List<FileOutConfig> fileOutConfigList = injectionConfig.getFileOutConfigList();
        if (CollectionUtil.isNotEmpty(fileOutConfigList)) {
            for (FileOutConfig fileOutConfig : fileOutConfigList) {
                try {
                    DwjFileOutConfig dwjFileOutConfig = (DwjFileOutConfig) fileOutConfig;
                    File tmpFile = File.createTempFile("template", ".ftl", tempDirectory);
                    outputStream = new FileOutputStream(tmpFile);
                    outputStream.write(dwjFileOutConfig.getProjectTemplate().getTemplateContent().getBytes());
                    dwjFileOutConfig.getProjectTemplate().setTemplateName(tmpFile.getName());
                } catch (IOException e) {
                    logger.error("生成代码模板临时文件异常", e);
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            logger.error("生成代码模板临时文件关闭流异常", e);
                        }
                    }
                }
            }
        }
        try {
            configuration.setDirectoryForTemplateLoading(tempDirectory);
        } catch (IOException e) {
            logger.error("设置模板加载器异常", e);
        }
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templateName, String outputFile) throws Exception {
        Template template = configuration.getTemplate(templateName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, ConstVal.UTF8)) {
            template.process(objectMap, outputStreamWriter);
        }
    }


    @Override
    public String templateFilePath(String filePath) {
        return null;
    }

    /**
     * 输出文件
     */
    public AbstractTemplateEngine batchOutput() {
        List<FileOutConfig> focList = null;
        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            for (TableInfo tableInfo : tableInfoList) {
                //字段名全部转小写开头
                fieldNaming(tableInfo);
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                // 自定义内容
                InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
                if (null != injectionConfig) {
                    objectMap.put("extraVariable", injectionConfig.getMap());
                    focList = injectionConfig.getFileOutConfigList();
                    setOutFileVariable(objectMap, focList, tableInfo);
                    if (CollectionUtils.isNotEmpty(focList)) {
                        for (FileOutConfig foc : focList) {
                            DwjFileOutConfig dwjFileOutConfig = (DwjFileOutConfig) foc;
                            if (isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
                                writer(objectMap, dwjFileOutConfig.getProjectTemplate().getTemplateName(), foc.outputFile(tableInfo));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }finally {
            //删除临时模板
            Object tempDirectory = getConfigBuilder().getInjectionConfig().getMap().get("temp_directory");
            if(tempDirectory != null){
                File tempDirectoryFile = (File) tempDirectory;
                if(ArrayUtil.isNotEmpty(tempDirectoryFile.listFiles()) && CollectionUtil.isNotEmpty(focList)){
                    for (File file : Objects.requireNonNull(tempDirectoryFile.listFiles())) {
                        for (FileOutConfig fileOutConfig : focList) {
                            DwjFileOutConfig dwjFileOutConfig = (DwjFileOutConfig) fileOutConfig;
                            if(file.exists() && dwjFileOutConfig.getProjectTemplate().getTemplateName().equals(file.getName())){
                                file.delete();
                            }
                        }
                    }
                }
            }
        }
        return this;
    }

    private void fieldNaming(TableInfo tableInfo) {
        if(CollectionUtil.isNotEmpty(tableInfo.getFields())){
            for (TableField field : tableInfo.getFields()) {
                field.setPropertyName(ConvertUtil.lowerCaseFirstLetter(field.getPropertyName()));
            }
        }
        List<TableField> commonFields = tableInfo.getCommonFields();
        if(CollectionUtil.isNotEmpty(commonFields)){
            for (TableField commonField : commonFields) {
                commonField.setPropertyName(ConvertUtil.lowerCaseFirstLetter(commonField.getPropertyName()));
            }
        }
    }

    public void setOutFileVariable(Map<String, Object> objectMap, List<FileOutConfig> focList, TableInfo tableInfo) {
        if(CollectionUtil.isNotEmpty(focList)){
            for (FileOutConfig fileOutConfig : focList) {
                DwjFileOutConfig dwjFileOutConfig = (DwjFileOutConfig) fileOutConfig;
                dwjFileOutConfig.getFileName(tableInfo);
                String fileType = ConvertUtil.lineToHump(dwjFileOutConfig.getProjectTemplate().getTemplateType().toString().toLowerCase());
                objectMap.put(fileType + "Package", dwjFileOutConfig.getProjectTemplate().getPackageName());
                objectMap.put(fileType + "ClassName", dwjFileOutConfig.getFileName(tableInfo));
            }
        }
    }

    /**
     * 处理输出目录
     */
    public AbstractTemplateEngine mkdirs() {
        List<FileOutConfig> fileOutConfigList = getConfigBuilder().getInjectionConfig().getFileOutConfigList();
        if(CollectionUtil.isNotEmpty(fileOutConfigList)){
            for (FileOutConfig fileOutConfig : fileOutConfigList) {
                DwjFileOutConfig dwjFileOutConfig = (DwjFileOutConfig) fileOutConfig;
                File dir = new File(dwjFileOutConfig.getProjectTemplate().getOutputPath());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
        }
        return this;
    }
}
