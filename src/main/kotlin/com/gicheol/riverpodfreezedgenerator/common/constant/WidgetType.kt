package com.gicheol.riverpodfreezedgenerator.common.constant

enum class WidgetType(
    val widgetName: String,
    val stateSuffix: String = "",
    val needsRiverpod: Boolean = false,
    val hasState: Boolean = false,
) {
    StatelessWidget(
        widgetName = "StatelessWidget",
        hasState = false,
    ),
    ConsumerWidget(
        widgetName = "ConsumerWidget",
        needsRiverpod = true,
        hasState = false,
    ),
    StatefulWidget(
        widgetName = "StatefulWidget",
        stateSuffix = "State",
        hasState = true,
    ),
    ConsumerStatefulWidget(
        widgetName = "ConsumerStatefulWidget",
        stateSuffix = "State",
        needsRiverpod = true,
        hasState = true,
    );

    fun getStateBaseClass(): String = when (this) {
        ConsumerStatefulWidget -> "ConsumerState"
        StatefulWidget -> "State"
        else -> throw IllegalStateException("StatelessWidget, ConsumerWidget is not State class")
    }

    fun getBuildMethodSignature(): String = when (this) {
        ConsumerWidget -> "Widget build(BuildContext context, WidgetRef ref)"
        StatelessWidget, StatefulWidget, ConsumerStatefulWidget -> "Widget build(BuildContext context)"
    }
}