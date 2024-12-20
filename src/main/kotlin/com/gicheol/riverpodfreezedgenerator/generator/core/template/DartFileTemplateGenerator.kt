package com.gicheol.riverpodfreezedgenerator.generator.core.template

import com.gicheol.riverpodfreezedgenerator.generator.constant.RadioType
import com.gicheol.riverpodfreezedgenerator.util.Utility

class DartFileTemplateGenerator {
    companion object {
        fun generateModelDartCode(
            dartFileName: String,
            model: String,
        ): String {
            val dartFileNameToSnakeToCamelCase = Utility.snakeToCamelCase(dartFileName)
            return """
            |import 'package:freezed_annotation/freezed_annotation.dart';

            |part '$dartFileName.freezed.dart';
            |part '$dartFileName.g.dart';
            
            |@freezed
            |abstract class $dartFileNameToSnakeToCamelCase with _$$dartFileNameToSnakeToCamelCase {
            |  const factory $dartFileNameToSnakeToCamelCase({
            $model
            |  }) = _$dartFileNameToSnakeToCamelCase;
            
            |  factory $dartFileNameToSnakeToCamelCase.fromJson(Map<String, dynamic> json) =>
            |      _$${dartFileNameToSnakeToCamelCase}FromJson(json);
            |}""".trimMargin()
        }

        fun generateProviderDartCode(
            dartFileName: String,
            model: String,
            onChange: String,
            type: RadioType,
        ): String {
            val dartFileNameToSnakeToCamelCase = Utility.snakeToCamelCase(dartFileName, type.value)

            return """
            |import 'package:freezed_annotation/freezed_annotation.dart';
            |import 'package:riverpod_annotation/riverpod_annotation.dart';

            |part '${dartFileName}.freezed.dart';
            |part '${dartFileName}.g.dart';

            |@Riverpod(keepAlive: true)
            |class ${dartFileNameToSnakeToCamelCase}Notifier extends _$${dartFileNameToSnakeToCamelCase}Notifier {
            |  @override
            |  ${dartFileNameToSnakeToCamelCase}State build() {
            |    return const ${dartFileNameToSnakeToCamelCase}State();
            |  }
            |  
            |  void clear() {
            |    state = build();
            |  }${if (onChange.trim().isNotEmpty()) "\n\n$onChange" else ""}
            |}

            |enum ${dartFileNameToSnakeToCamelCase}Status {
            |  init,
            |  success,
            |  error,
            |}

            |@freezed
            |abstract class ${dartFileNameToSnakeToCamelCase}State with _$${dartFileNameToSnakeToCamelCase}State {
            |  const factory ${dartFileNameToSnakeToCamelCase}State({${if (model.trim().isNotEmpty()) "\n$model" else ""}
            |    @Default(${dartFileNameToSnakeToCamelCase}Status.init) ${
                dartFileNameToSnakeToCamelCase
            }Status status,
            |    // TODO: Add a state field for error handling.
            |  }) = _${dartFileNameToSnakeToCamelCase}State;

            |  factory ${dartFileNameToSnakeToCamelCase}State.fromJson(Map<String, dynamic> json) =>
            |      _$${dartFileNameToSnakeToCamelCase}StateFromJson(json);
            |}""".trimMargin()
        }

        fun generateServiceDartCode(dartFileName: String): String {
            val dartFileNameToSnakeToCamelCase = Utility.snakeToCamelCase(dartFileName)
            val dartFileNameToSnakeToCamelCaseFirstLowerCase = Utility.snakeToCamelCaseFirstLowerCase(dartFileName)
            return """
            |import 'package:dio/dio.dart';
            |import 'package:flutter_riverpod/flutter_riverpod.dart';
            
            |class $dartFileNameToSnakeToCamelCase {
            |  final Ref ref;
            
            |  $dartFileNameToSnakeToCamelCase(this.ref);
            
            |  Future<Response<dynamic>> load() async {
            |    try {
            |      // TODO: Remove return null!; and implement the business logic.
            |      // final response = await ref.read(requestApiProvider).get(
            |      //      path: RestApiUri.,
            |      //    );
            
            |      // return response;
            |      
            |      return null!;
            |    } on DioException catch (e) {
            |      // TODO: Add an Exception logger
            |      // logger.e("DioException: ${"$"}{e.message}");
            |      return Future.error(e);
            |    } on Exception catch (e) {
            |      // TODO: Add an Exception logger
            |      // logger.e(e);
            |      return Future.error(e);
            |    }
            |  }
            |}
            
            |final ${dartFileNameToSnakeToCamelCaseFirstLowerCase}Provider =
            |    Provider<$dartFileNameToSnakeToCamelCase>((ref) => $dartFileNameToSnakeToCamelCase(ref));""".trimMargin()
        }
    }
}