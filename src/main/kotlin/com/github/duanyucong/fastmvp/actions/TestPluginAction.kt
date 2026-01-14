package com.github.duanyucong.fastmvp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile

class TestPluginAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val editor = event.getData(CommonDataKeys.EDITOR)
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)
        
        if (editor == null || virtualFile == null) {
            return
        }
        
        // 获取当前类的信息
        val className = virtualFile.name.replace(".java", "")
        
        // 根据类名后缀判断是Activity还是Fragment
        val isActivity = className.endsWith("Activity")
        val isFragment = className.endsWith("Fragment")
        
        if (!isActivity && !isFragment) {
            return
        }
        
        // 获取包名
        val fileContent = editor.document.text
        val packageNameMatch = Regex("package\\s+([^;]+);").find(fileContent)
        val packageName = packageNameMatch?.groupValues?.get(1) ?: return
        
        // 生成MVP代码
        val codeGenerator = CodeGenerator(project)
        val logs = codeGenerator.generate(
            className.dropLast(if (isActivity) 8 else 9), // 移除Activity或Fragment后缀
            packageName,
            isActivity,
            true // 默认生成Dagger组件
        )
        
        // 显示执行记录和日志
        LogDialog(project, logs).show()
    }
}