package com.github.duanyucong.fastmvp.actions

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.nio.file.Paths

class CodeGenerator(private val project: Project) {
    
    private val logs = mutableListOf<String>()
    
    // 从当前文件路径生成
    fun generateFromCurrentFile(className: String, packageName: String, isActivity: Boolean, generateDagger: Boolean, targetDir: String): List<String> {
        logs.clear()
        addLog("Starting MVP code generation for $className")
        addLog("Package: $packageName")
        addLog("Type: ${if (isActivity) "Activity" else "Fragment"}")
        addLog("Generate Dagger components: $generateDagger")
        addLog("Target directory: $targetDir")
        
        thisLogger().info("Generating MVP code for $className, package: $packageName, isActivity: $isActivity, generateDagger: $generateDagger, targetDir: $targetDir")
        
        // 确定模板类型（Activity或Fragment）
        val templateType = if (isActivity) "activity" else "fragment"
        
        // 直接在目标目录下生成文件
        copyTemplateFile(templateType, templateType.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }, className, packageName, targetDir)
        copyTemplateFile(templateType, "Contract", className, packageName, targetDir)
        copyTemplateFile(templateType, "Model", className, packageName, targetDir)
        copyTemplateFile(templateType, "Presenter", className, packageName, targetDir)
        
        // 如果需要生成Dagger组件，直接在目标目录下创建dagger文件夹
        if (generateDagger) {
            val daggerPath = Paths.get(targetDir, "dagger").toString()
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
            
            addLog("Created Dagger directories in target dir: $daggerPath")
            
            copyTemplateFile(templateType, "component", className, packageName, componentPath)
            copyTemplateFile(templateType, "module", className, packageName, modulePath)
        }
        
        addLog("MVP code generation completed for $className")
        thisLogger().info("MVP code generation completed for $className")
        return logs
    }
    
    // 兼容旧方法，用于对话框生成
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
        
        // 调用新的生成方法
        return generateFromCurrentFile(className, packageName, isActivity, generateDagger, srcPath)
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
            // 检查文件是否已存在
            val fileExisted = destFile.exists()
            
            // 写入文件（会自动覆写已存在的文件）
            destFile.writeText(processedContent)
            
            if (fileExisted) {
                addLog("Overwrote file: ${destFile.absolutePath}")
                thisLogger().info("Overwrote file: ${destFile.absolutePath}")
            } else {
                addLog("Generated file: ${destFile.absolutePath}")
                thisLogger().info("Generated file: ${destFile.absolutePath}")
            }
            
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
            val localFileSystem = LocalFileSystem.getInstance()
            
            // 1. 刷新整个项目的根目录，确保VFS完全更新
            project.basePath?.let {
                val projectRootVirtualFile = localFileSystem.findFileByPath(it)
                if (projectRootVirtualFile != null) {
                    projectRootVirtualFile.refresh(true, true) // 递归刷新所有子目录
                    addLog("Project root VFS refresh completed: ${projectRootVirtualFile.path}")
                }
            }
            
            // 2. 再次尝试刷新父目录
            val parentPath = destFile.parentFile?.absolutePath
            if (parentPath != null) {
                val parentVirtualFile = localFileSystem.refreshAndFindFileByPath(parentPath)
                if (parentVirtualFile != null) {
                    parentVirtualFile.refresh(false, true)
                    addLog("Parent directory VFS refresh completed: $parentPath")
                }
            }
            
            // 3. 最后尝试获取并刷新文件
            val virtualFile = localFileSystem.refreshAndFindFileByPath(destFile.absolutePath)
            if (virtualFile != null) {
                virtualFile.refresh(false, true)
                addLog("VFS refresh completed for: ${destFile.absolutePath}")
                thisLogger().info("VFS refresh completed for: ${destFile.absolutePath}")
            } else {
                addLog("VFS refresh: File found in VFS after global refresh")
                thisLogger().info("VFS refresh: File found in VFS after global refresh")
            }
        } catch (e: Exception) {
            val errorMsg = "Error writing file ${destFile.absolutePath}: ${e.message}"
            addLog(errorMsg)
            thisLogger().error(errorMsg, e)
        }
    }
}