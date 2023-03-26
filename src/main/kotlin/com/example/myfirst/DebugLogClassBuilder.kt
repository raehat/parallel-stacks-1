package com.example.myfirst

import org.jetbrains.kotlin.codegen.ClassBuilder
import org.jetbrains.kotlin.codegen.DelegatingClassBuilder
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin
import org.jetbrains.org.objectweb.asm.MethodVisitor

internal class DebugLogClassBuilder (delegateBuilder: ClassBuilder) : DelegatingClassBuilder()
{
    override fun newMethod(
        origin: JvmDeclarationOrigin,
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val original = super.newMethod(origin, access, name, desc, signature, exceptions)

        println("method visited")

        return original
    }

    override fun getDelegate(): ClassBuilder {
        return delegate
    }
}