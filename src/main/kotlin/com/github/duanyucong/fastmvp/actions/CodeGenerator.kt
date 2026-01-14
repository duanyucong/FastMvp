package com.github.duanyucong.fastmvp.actions

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.nio.file.Paths

class CodeGenerator(private val project: Project) {
    
    private val logs = mutableListOf<String>()
    
    fun generate(className: String, packageName: String, isActivity: Boolean, generateDagger: Boolean): List<String> {
        logs.clear()
        addLog("Starting MVP code generation for $className")
        addLog("Package: $packageName")
        addLog("Type: ${if (isActivity) "Activity" else "Fragment"}")
        addLog("Generate Dagger components: $generateDagger")
        
        thisLogger().info("Generating MVP code for $className, package: $packageName, isActivity: $isActivity, generateDagger: $generateDagger")
        
        // 获取项目根目录
        val projectRoot = project.basePath ?: return logs.apply { addLog("Error: Project root not found") }
        addLog("Project root: $projectRoot")
        
        // 确定模板类型（Activity或Fragment）
        val templateType = if (isActivity) "activity" else "fragment"
        
        // 创建包目录结构
        val packageParts = packageName.split('.')
        val srcPath = Paths.get(projectRoot, "src", "main", "java", *packageParts.toTypedArray()).toString()
        val srcDir = File(srcPath)
        if (!srcDir.exists()) {
            val created = srcDir.mkdirs()
            if (!created) {
                val errorMsg = "Error: Failed to create package directory: $srcPath"
                addLog(errorMsg)
                thisLogger().error(errorMsg)
                return logs
            }
        }
        addLog("Created package directory: $srcPath")
        
        // 复制并修改模板文件
        copyTemplateFile(templateType, templateType.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }, className, packageName, srcPath)
        copyTemplateFile(templateType, "Contract", className, packageName, srcPath)
        copyTemplateFile(templateType, "Model", className, packageName, srcPath)
        copyTemplateFile(templateType, "Presenter", className, packageName, srcPath)
        
        // 如果需要生成Dagger组件
        if (generateDagger) {
            val daggerPath = Paths.get(srcPath, "dagger").toString()
            val componentPath = Paths.get(daggerPath, "component").toString()
            val modulePath = Paths.get(daggerPath, "module").toString()
            
            // 创建Dagger目录
            val componentDir = File(componentPath)
            val moduleDir = File(modulePath)
            
            if (!componentDir.exists()) {
                val created = componentDir.mkdirs()
                if (!created) {
                    val errorMsg = "Error: Failed to create component directory: $componentPath"
                    addLog(errorMsg)
                    thisLogger().error(errorMsg)
                }
            }
            
            if (!moduleDir.exists()) {
                val created = moduleDir.mkdirs()
                if (!created) {
                    val errorMsg = "Error: Failed to create module directory: $modulePath"
                    addLog(errorMsg)
                    thisLogger().error(errorMsg)
                }
            }
            
            addLog("Created Dagger directories")
            
            copyTemplateFile(templateType, "component", className, packageName, componentPath)
            copyTemplateFile(templateType, "module", className, packageName, modulePath)
        }
        
        addLog("MVP code generation completed for $className")
        thisLogger().info("MVP code generation completed for $className")
        return logs
    }
    
    private fun addLog(message: String) {
        logs.add("[${java.time.LocalDateTime.now()}] $message")
    }
    
    private fun copyTemplateFile(templateType: String, fileType: String, className: String, packageName: String, destPath: String) {
        // 确定模板文件名（使用.txt文件作为模板）
        val templateFileName = when (fileType) {
            "Activity", "Fragment", "Contract", "Model", "Presenter" -> "$fileType.txt"
            "component" -> "Component.txt"
            "module" -> "Module.txt"
            else -> ""
        }
        
        // 确定目标文件名
        val destFileName = when (fileType) {
            "Activity", "Fragment" -> "${className}$fileType.java"
            "Contract", "Model", "Presenter" -> "${className}$fileType.java"
            "component" -> "${className}Component.java"
            "module" -> "${className}Module.java"
            else -> ""
        }
        
        // 构建模板文件的资源路径
        val resourcePath = when (fileType) {
            "component" -> "/templates/$templateType/dagger/component/$templateFileName"
            "module" -> "/templates/$templateType/dagger/module/$templateFileName"
            else -> "/templates/$templateType/$templateFileName"
        }
        
        addLog("Processing template: $resourcePath")
        
        // 读取模板内容
        val content = javaClass.getResourceAsStream(resourcePath)?.bufferedReader()?.readText()
        if (content == null) {
            val errorMsg = "Error: Template file not found in resources: $resourcePath"
            addLog(errorMsg)
            thisLogger().error(errorMsg)
            return
        }
        
        // 替换占位符
        var processedContent = content
            .replace("\${packageName}", packageName)
            .replace("\${className}", className)
        
        // 写入新文件
        val destFile = File(destPath, destFileName)
        try {
            destFile.writeText(processedContent)
            addLog("Generated file: ${destFile.absolutePath}")
            thisLogger().info("Generated file: ${destFile.absolutePath}")
            
            // 验证文件是否实际存在且内容正确
            if (destFile.exists()) {
                addLog("Verification: File exists: ${destFile.absolutePath}")
                addLog("Verification: File size: ${destFile.length()} bytes")
            } else {
                val errorMsg = "Error: File was not created despite write operation: ${destFile.absolutePath}"
                addLog(errorMsg)
                thisLogger().error(errorMsg)
            }
            
            // 刷新VFS，使IntelliJ能够识别新文件
            val virtualFile = LocalFileSystem.getInstance().findFileByPath(destFile.absolutePath)
            if (virtualFile != null) {
                virtualFile.refresh(false, true)
                addLog("VFS refresh completed for: ${destFile.absolutePath}")
            } else {
                addLog("VFS refresh failed: Could not find virtual file for: ${destFile.absolutePath}")
            }
        } catch (e: Exception) {
            val errorMsg = "Error writing file ${destFile.absolutePath}: ${e.message}"
            addLog(errorMsg)
            thisLogger().error(errorMsg, e)
        }
    }
}