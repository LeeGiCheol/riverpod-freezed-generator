# Flutter Riverpod Freezed Generator

<!-- Plugin description -->
## Preview
![setup image](https://raw.githubusercontent.com/LeeGiCheol/riverpod-freezed-generator/refs/heads/main/image/overview.gif)

Project Github [https://github.com/LeeGiCheol/riverpod-freezed-generator](https://github.com/LeeGiCheol/riverpod-freezed-generator)  
Created using [https://github.com/JetBrains/intellij-platform-plugin-template](https://github.com/JetBrains/intellij-platform-plugin-template)


[![official JetBrains project](https://jb.gg/badges/official.svg)](https://github.com/JetBrains/.github/blob/main/profile/README.md)
[![Twitter Follow](https://img.shields.io/badge/follow-%40JBPlatform-1DA1F2?logo=twitter)](https://twitter.com/JBPlatform)
[![Build](https://github.com/JetBrains/intellij-platform-plugin-template/workflows/Build/badge.svg)](https://github.com/JetBrains/intellij-platform-plugin-template/actions?query=workflow%3ABuild)
[![Slack](https://img.shields.io/badge/Slack-%23intellij--platform-blue?style=flat-square&logo=Slack)](https://plugins.jetbrains.com/slack)

![IntelliJ Platform Plugin Template](./.github/readme/intellij-platform-plugin-template-dark.svg#gh-dark-mode-only)
![IntelliJ Platform Plugin Template](./.github/readme/intellij-platform-plugin-template-light.svg#gh-light-mode-only)

## Overview

This plugin automatically generates `*.freezed.dart` and `*.g.dart` files for creating Models, Providers, and Services when using Flutter Riverpod.

### Key Features

- **JSON to Model**: Enter JSON in the JSON Editor to generate Freezed State Models for both Model and Provider. (Service does not support Model generation)
- **Supported Types**:
    - **`int`**, **`double`**, **`String`**, **`bool`**, **`Object`**, **`Array`**
    - Default annotation values use the default values for each type.
    - Nested JSON is supported
    - JSON starting with an Array is not supported
- **onChange Function**: Available when Provider is selected and JSON Object is entered.

**Note**: Dart files are generated, but build is not automated.

- **Switching Widget** Supports switching between StatelessWidget, StatefulWidget, ConsumerWidget  
and ConsumerStatefulWidget through Context Actions (Windows: Alt + Enter, Mac: Option + Enter).

## Freezed Build Command

Access the terminal and run:

```bash
flutter pub run build_runner build --delete-conflicting-outputs
```

## Usage

1. Install the plugin in your IDE (IntelliJ, Android Studio):
    - Settings > Plugins > Install Plugin from Disk... > Select jar file
2. Right-click on the directory where you want to create dart files, select New > Riverpod Freezed Generator
3. When writing the Dart file name, omit the .dart extension.
4. Use snake_case for file names and camelCase for class names.
---

# Flutter Riverpod Freezed 생성기

## 개요

이 플러그인은 Flutter Riverpod을 사용할 때 Model, Provider, Service를 위한 `*.freezed.dart`와 `*.g.dart` 파일을 자동으로 생성해주는 기능을 제공합니다.

## 주요 기능

- **JSON to Model**: JSON Editor에 JSON을 입력하면 Model과 Provider에 대한 Freezed State Model을 생성합니다. (Service는 Model 생성을 지원하지 않습니다)
- **지원 타입**:
    - `int`, `double`, `String`, `bool`, `Object`, `Array`
    - Default Annotation의 기본값은 각 타입의 기본값을 사용합니다.
    - 중첩 JSON은 지원하지 않습니다.
    - Array로 시작하는 JSON은 지원하지 않습니다.
- **onChange 함수 생성**: Provider 선택 및 JSON Object 입력 시 사용 가능합니다.

**참고**: dart 파일 생성 후 자동 빌드는 지원하지 않습니다.

- **Widget 전환** : StatelessWidget, StatefulWidget, ConsumerWidget, ConsumerStatefulWidget 간 전환을  
Context Action(Windows: Alt + Enter, Mac: Option + Enter)을 통해 간편하게 지원합니다.

## Freezed 빌드 명령어

터미널에 접속하여 다음 명령어를 실행하세요:

```bash
flutter pub run build_runner build --delete-conflicting-outputs
```

## 사용법

1. plugin을 IDE (IntelliJ, Android Studio)에 설치합니다.
    - Settings > Plugins > Install Plugin from Disk... > jar 선택하여 추가
2. dart 파일을 생성할 directory에 우측 클릭을 하여 New를 선택 > Riverpod Freezed Generator 실행
3. dart 파일명을 작성할 때 .dart는 생략합니다.
4. 파일명은 snake case, Class명은 calmel case를 사용합니다.
<!-- Plugin description end -->