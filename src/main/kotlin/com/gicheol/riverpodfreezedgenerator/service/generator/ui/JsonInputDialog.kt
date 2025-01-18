package com.gicheol.riverpodfreezedgenerator.service.generator.ui

import com.gicheol.riverpodfreezedgenerator.common.constant.RadioType
import com.intellij.json.JsonFileType
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JRadioButton
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.text.JTextComponent


var serviceButton = JRadioButton("service")
private val jsonInputDialogValidator: JsonInputDialogValidator = JsonInputDialogValidator()

class JsonInputDialog(
    project: Project,
) : Messages.InputDialog(
    project,
    "Enter in JSON format.",
    "",
    null,
    "",
    jsonInputDialogValidator
) {
    private lateinit var jsonContentEditor: Editor
    private lateinit var onChangeCheckBox: JCheckBox
    private lateinit var isBuildRunCheckBox: JCheckBox
    private lateinit var modelButton: JRadioButton
    private lateinit var providerButton: JRadioButton

    init {
        setOKButtonText("Generate")
    }

    override fun createCenterPanel(): JComponent? {
        jsonContentEditor = createJsonContentEditor()
        jsonInputDialogValidator.jsonInputEditor = jsonContentEditor
        
        val mainPanel = JPanel(BorderLayout())

        // JSON Editor 영역
        val editorScrollPane = JScrollPane(jsonContentEditor.component).apply {
            preferredSize = Dimension(640, 480)
        }
        mainPanel.add(editorScrollPane, BorderLayout.CENTER)

        // 아래 패널 구성
        val bottomPanel = JPanel()
        bottomPanel.layout = BoxLayout(bottomPanel, BoxLayout.Y_AXIS)

        // Dart File Name
        val namePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        namePanel.add(JLabel("Dart File Name: "))
        myField = JTextField("", 100)
        namePanel.add(myField)

        bottomPanel.add(namePanel)
        bottomPanel.add(Box.createVerticalStrut(7))

        // build command clipboard save
        val clipboardPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val clipboardSaveButton = JButton("save clipboard")
        clipboardSaveButton.addActionListener {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val text = "flutter pub run build_runner build --delete-conflicting-outputs"
            val transferable = StringSelection(text)
            clipboard.setContents(transferable, null)
        }
        clipboardPanel.add(JLabel("build: flutter pub run build_runner build --delete-conflicting-outputs"))
        clipboardPanel.add(clipboardSaveButton)

        bottomPanel.add(clipboardPanel)

        // Radio Buttons
        val buttonGroup = ButtonGroup()
        modelButton = JRadioButton("model")
        providerButton = JRadioButton("provider")
        buttonGroup.add(modelButton)
        buttonGroup.add(providerButton)
        buttonGroup.add(serviceButton)
        modelButton.isSelected = true

        val radioPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        radioPanel.add(modelButton)
        radioPanel.add(providerButton)
        radioPanel.add(serviceButton)
        bottomPanel.add(radioPanel)

        // CheckBox 추가
        val checkBoxPanel = JPanel(FlowLayout(FlowLayout.LEFT))

        isBuildRunCheckBox = JCheckBox("Build Run")
        isBuildRunCheckBox.isSelected = true
        checkBoxPanel.add(isBuildRunCheckBox)
        bottomPanel.add(checkBoxPanel)

        onChangeCheckBox = JCheckBox("add onChange")
        checkBoxPanel.add(onChangeCheckBox)
        bottomPanel.add(checkBoxPanel)

        bottomPanel.add(Box.createVerticalStrut(3))

        addListener()

        onChangeCheckBox.isEnabled = false

        mainPanel.add(bottomPanel, BorderLayout.SOUTH)

        return mainPanel
    }

    private fun addListener() {
        providerButton.addActionListener {
            // jsonEditor readonly + 색상 초기화
            jsonContentEditor.document.setReadOnly(false)
            val markupModel = jsonContentEditor.markupModel
            markupModel.removeAllHighlighters()

            onChangeCheckBox.isEnabled = providerButton.isSelected
            okAction.isEnabled = jsonInputDialogValidator.checkInput(jsonContentEditor.document.text) && myField.text.isNotEmpty()
        }
        modelButton.addActionListener {
            // jsonEditor readonly + 색상 초기화
            jsonContentEditor.document.setReadOnly(false)
            val markupModel = jsonContentEditor.markupModel
            markupModel.removeAllHighlighters()

            onChangeCheckBox.isEnabled = false
            okAction.isEnabled = jsonInputDialogValidator.checkInput(jsonContentEditor.document.text) && myField.text.isNotEmpty()
        }
        // service button 클릭 시 json editor 입력 받지 않음
        serviceButton.addActionListener {
            isBuildRunCheckBox.isEnabled = false
            onChangeCheckBox.isEnabled = false
            okAction.isEnabled = myField.text.isNotEmpty()

            // jsonEditor readonly + 색상 변경
            jsonContentEditor.document.setReadOnly(true)
            val markupModel = jsonContentEditor.markupModel
            markupModel.removeAllHighlighters()

            val textAttributes = TextAttributes()
            textAttributes.foregroundColor = Color.GRAY

            val document = jsonContentEditor.document
            markupModel.addRangeHighlighter(
                0, document.textLength,
                HighlighterLayer.ADDITIONAL_SYNTAX,
                textAttributes,
                HighlighterTargetArea.EXACT_RANGE,
            )
        }

        myField.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent?) {
                if (serviceButton.isSelected) {
                    okAction.isEnabled = myField.text.isNotEmpty()
                } else {
                    okAction.isEnabled = jsonInputDialogValidator.checkInput(jsonContentEditor.document.text) && myField.text.isNotEmpty()
                }
            }
        })
    }

    fun getJsonInputString(): String {
        return jsonContentEditor.document.text
    }

    fun getOnchangeCheckBoxClicked(): Boolean {
        return onChangeCheckBox.isEnabled && onChangeCheckBox.isSelected
    }

    fun getIsBuildRunCheckBoxClicked(): Boolean {
        return isBuildRunCheckBox.isEnabled && isBuildRunCheckBox.isSelected
    }

    fun getSelectedRadioOption(): RadioType {
        return when {
            modelButton.isSelected -> RadioType.MODEL
            providerButton.isSelected -> RadioType.PROVIDER
            serviceButton.isSelected -> RadioType.SERVICE
            else -> RadioType.MODEL
        }
    }

    private fun createJsonContentEditor(): Editor {
        val editorFactory = EditorFactory.getInstance()
        val document = editorFactory.createDocument("").apply {
            setReadOnly(false)
            addDocumentListener(object : com.intellij.openapi.editor.event.DocumentListener {
                override fun documentChanged(event: DocumentEvent) = revalidate()
                override fun beforeDocumentChange(event: DocumentEvent) = Unit
            })
        }

        val editor = editorFactory.createEditor(document, null, JsonFileType.INSTANCE, false)

        editor.component.apply {
            isEnabled = true
            preferredSize = Dimension(640, 480)
            autoscrolls = true
        }

        val contentComponent = editor.contentComponent
        contentComponent.isFocusable = true
        contentComponent.componentPopupMenu = JPopupMenu().apply {
            add(createPasteFromClipboardMenuItem())
        }

        return editor
    }

    private fun createPasteFromClipboardMenuItem() = JMenuItem("Paste from clipboard").apply {
        addActionListener {
            val transferable = Toolkit.getDefaultToolkit().systemClipboard.getContents(null)
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                runWriteAction {
                    jsonContentEditor.document.setText(transferable.getTransferData(DataFlavor.stringFlavor).toString())
                }
            }
        }
    }

    fun getDartFileName(): String {
        return if (exitCode == 0) {
            val name = myField.text.trim()
            name.let { if (it.first().isDigit() || it.contains('$')) "`$it`" else it }
        } else ""
    }

    override fun createTextFieldComponent(): JTextComponent {
        val textField = JTextField()
        textField.preferredSize = Dimension(0, 0)
        textField.maximumSize = Dimension(0, 0)
        return textField
    }

    override fun createNorthPanel(): JComponent? {
        val panel = super.createNorthPanel()
        if (panel is JPanel) {
            panel.remove(myField)
        }
        return panel
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return jsonContentEditor.contentComponent
    }

    private fun revalidate() {
        if (serviceButton.isSelected) {
            return
        }

        okAction.isEnabled = jsonInputDialogValidator.checkInput(myField.text)
    }
}