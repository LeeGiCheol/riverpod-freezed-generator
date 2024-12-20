package com.gicheol.riverpodfreezedgenerator.service.converter.core.generator

import com.gicheol.riverpodfreezedgenerator.common.constant.WidgetType
import com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.widget.StatefulWidgetGenerator
import com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.widget.StatelessWidgetGenerator
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartComponent

class WidgetCodeGenerator {
    companion object {
        private val statelessGenerator = StatelessWidgetGenerator()
        private val statefulGenerator = StatefulWidgetGenerator()

        fun generateWidgetCode(
            classElement: DartClass,
            stateClass: DartClass,
            methods: List<DartComponent>,
            fields: MutableMap<String, String>,
            fromType: WidgetType,
            toType: WidgetType,
        ): String {
            val constructor = when {
                isFromStatefulOrConsumer(fromType) -> {
                    classElement.constructors.firstOrNull()?.text
                        ?: "  const ${classElement.name}({super.key});\n"
                }
                else -> {
                    stateClass.constructors.firstOrNull()?.text
                        ?: "  const ${classElement.name}({super.key});\n"
                }
            }
            return when {
                !toType.hasState -> {
                    statelessGenerator.generate(
                        classElement = classElement,
                        stateClass = stateClass,
                        stateClassMethods = methods,
                        constructor = constructor,
                        fields = fields,
                        toType = toType,
                    )
                }
                else -> {
                    statefulGenerator.generate(
                        classElement = classElement,
                        stateClass = stateClass,
                        stateClassMethods = methods,
                        constructor = constructor,
                        fields = fields,
                        fromType = fromType,
                        toType = toType,
                    )
                }
            }
        }

        private fun isFromStatefulOrConsumer(
            fromType: WidgetType,
        ) = (fromType == WidgetType.StatefulWidget || fromType == WidgetType.ConsumerStatefulWidget)
    }
}