package com.gicheol.riverpodfreezedgenerator.converter.filtype

enum class TargetWidgetType(val stateSuffix: String) {
    ConsumerWidget(""),
    ConsumerStatefulWidget("State"),
    StatefulWidget("State"),
    StatelessWidget("")
}