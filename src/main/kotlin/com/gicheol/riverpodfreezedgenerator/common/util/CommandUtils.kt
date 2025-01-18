package com.gicheol.riverpodfreezedgenerator.common.util

import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.project.Project
import io.flutter.pub.PubRoot
import io.flutter.sdk.FlutterSdk
import java.util.concurrent.CompletableFuture

class CommandUtils {
    companion object {
        fun executeFlutterPubCommand(
            project: Project,
            root: PubRoot,
            args: String,
            processListener: ProcessListener? = null,
            onDone: Runnable? = null,
        ): CompletableFuture<Unit> {
            val future = CompletableFuture<Unit>()

            val sdk = FlutterSdk.getFlutterSdk(project) ?: return future.apply {
                completeExceptionally(IllegalStateException("Flutter SDK configuration not found."))
            }
            val module = root.getModule(project) ?: return future.apply {
                completeExceptionally(IllegalStateException("Flutter Module not found."))
            }
            val command = sdk.flutterPub(root, *args.split(' ').toTypedArray())
            command.startInModuleConsole(
                module,
                {
                    future.complete(Unit)
                    onDone?.run()
                },
                processListener,
            )

            return future
        }
    }
}