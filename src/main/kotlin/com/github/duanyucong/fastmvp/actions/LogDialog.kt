package com.github.duanyucong.fastmvp.actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import javax.swing.JComponent

class LogDialog(project: Project, private val logs: List<String>) : DialogWrapper(project) {

    init {
        title = "FastMvp Generation Log"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val textArea = JBTextArea(20, 60)
        textArea.isEditable = false
        textArea.text = logs.joinToString("\n")
        return JBScrollPane(textArea)
    }
}
