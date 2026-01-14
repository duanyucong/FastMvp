package com.github.duanyucong.fastmvp.actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.ButtonGroup

class GenerateMvpDialog(private val project: Project) : DialogWrapper(project) {

    private val classNameField = JBTextField()
    private val packageNameField = JBTextField()
    private val activityRadioButton = JRadioButton("Activity")
    private val fragmentRadioButton = JRadioButton("Fragment")
    private val daggerRadioButton = JRadioButton("Generate Dagger Components")
    
    init {
        title = "Generate MVP Code"
        init()
        
        // 设置默认选择Fragment
        fragmentRadioButton.isSelected = true
    }
    
    override fun createCenterPanel(): JComponent {
        val radioGroup = ButtonGroup()
        radioGroup.add(activityRadioButton)
        radioGroup.add(fragmentRadioButton)
        
        return FormBuilder.createFormBuilder()
            .addLabeledComponent("Class Name:", classNameField)
            .addLabeledComponent("Package Name:", packageNameField)
            .addVerticalGap(10)
            .addComponent(activityRadioButton)
            .addComponent(fragmentRadioButton)
            .addVerticalGap(10)
            .addComponent(daggerRadioButton)
            .addVerticalGap(10)
            .panel
    }
    
    fun getClassName(): String {
        return classNameField.text.trim()
    }
    
    fun getPackageName(): String {
        return packageNameField.text.trim()
    }
    
    fun isActivity(): Boolean {
        return activityRadioButton.isSelected
    }
    
    fun isFragment(): Boolean {
        return fragmentRadioButton.isSelected
    }
    
    fun generateDagger(): Boolean {
        return daggerRadioButton.isSelected
    }
    
    override fun doOKAction() {
        // 验证输入
        if (getClassName().isEmpty() || getPackageName().isEmpty()) {
            return
        }
        
        // 调用代码生成服务
        val codeGenerator = CodeGenerator(project)
        codeGenerator.generate(
            getClassName(),
            getPackageName(),
            isActivity(),
            generateDagger()
        )
        
        super.doOKAction()
    }
}