package com.example.myfirst

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.content.ContentFactory
import com.intellij.util.messages.MessageBusConnection
import com.intellij.xdebugger.*
import com.intellij.xdebugger.breakpoints.*
import com.intellij.xdebugger.frame.*
import com.intellij.xdebugger.frame.presentation.XValuePresentation
import com.intellij.xdebugger.impl.ui.tree.nodes.XEvaluationCallbackBase
import com.intellij.xdebugger.impl.ui.tree.nodes.XValueNodePresentationConfigurator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.debug.DebugProbes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class MyToolWindow : ToolWindowFactory, XBreakpointListener<XBreakpoint<*>> {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {



        DebugLogComponentRegistrar()
        DebugLogCommandLineProcessor()

        val content: JPanel = JPanel()
        toolWindow.contentManager.addContent(
            ContentFactory.SERVICE.getInstance().createContent(content, "", false)
        )

        val xDebuggerManager = XDebuggerManager.getInstance(project)

//        for (executionStack in executionStacks) {
//            val frameproxy = executionStack
//        }

        val breakpointManager = xDebuggerManager.breakpointManager

        val connection: MessageBusConnection = project.messageBus.connect(project)
//        connection.subscribe(XBreakpointListener.TOPIC, object : XBreakpointListener<XBreakpoint<*>> {
//            override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
//                super.breakpointAdded(breakpoint)
//            }
//
//            override fun breakpointRemoved(breakpoint: XBreakpoint<*>) {
//                super.breakpointRemoved(breakpoint)
//                Messages.showMessageDialog("break point removed", "Hellooo", Messages.getInformationIcon())
//            }
//
//            override fun breakpointChanged(breakpoint: XBreakpoint<*>) {
//                super.breakpointChanged(breakpoint)
//                Messages.showMessageDialog("break point changed", "Hellooo", Messages.getInformationIcon())
//            }
//
//            override fun breakpointPresentationUpdated(breakpoint: XBreakpoint<*>, session: XDebugSession?) {
//                super.breakpointPresentationUpdated(breakpoint, session)
//                Messages.showMessageDialog("break point presentation updated", "Hellooo", Messages.getInformationIcon())
//            }
//        })

//        for (i in xDebuggerManager.breakpointManager.allBreakpoints)
//            println(i.sourcePosition)

        if (xDebuggerManager.currentSession != null) {
            println("lmaoooo")
        }

        var parallelStacksOn = false

        val button1 = JButton("start debuggin")

        button1.addActionListener {

            if (xDebuggerManager.currentSession == null) {
                Messages.showMessageDialog(
                    "Start debuggin first!!", "", Messages.getInformationIcon()
                )
            } else {

                Messages.showMessageDialog(
                    "Debugging started!", "", Messages.getInformationIcon()
                )

                val currentDebugPosition = JLabel("")
                currentDebugPosition.text = xDebuggerManager.currentSession?.currentPosition?.line.toString()

                content.add(currentDebugPosition)

                println("bool : ${xDebuggerManager.currentSession?.currentPosition?.line}")

                xDebuggerManager.currentSession?.addSessionListener(object : XDebugSessionListener {
                    override fun sessionPaused() {
                        super.sessionPaused()
                        currentDebugPosition.text = xDebuggerManager.currentSession?.currentPosition?.line.toString()
                        println("bool : ${xDebuggerManager.currentSession?.currentPosition?.line}")

                        val susppendContext : XSuspendContext = xDebuggerManager.currentSession!!.suspendContext

                        val executor = Executors.newSingleThreadExecutor()

                        executor.execute {

                            println("support for code fragment or not : ${susppendContext.activeExecutionStack!!.topFrame!!.evaluator?.isCodeFragmentEvaluationSupported}")

                            val evaluator = susppendContext.activeExecutionStack!!.topFrame?.evaluator

                            susppendContext.activeExecutionStack!!.topFrame?.evaluator
                                ?.evaluate("DebugProbes.dumpCoroutinesInfo()", object : XEvaluationCallbackBase() {
                                    override fun errorOccurred(errorMessage: String) {
                                        println("errorOcc $errorMessage")
                                    }

                                    override fun evaluated(result: XValue) {

                                        println("result $result")

                                            result.computeChildren(object : XCompositeNode {
                                                override fun addChildren(children: XValueChildrenList, last: Boolean) {
                                                    try {
                                                        if (children.size() > 0) {

                                                            println("addChildren ${children.getValue(0)} ${children.getName(0)}")

                                                            try {

                                                                val res = children.getValue(0)

                                                                res?.computeChildren(object : XCompositeNode {
                                                                    override fun addChildren(
                                                                        children: XValueChildrenList,
                                                                        last: Boolean
                                                                    ) {

                                                                    }

                                                                    override fun tooManyChildren(remaining: Int) {
                                                                    }

                                                                    override fun setAlreadySorted(alreadySorted: Boolean) {
                                                                    }

                                                                    override fun setErrorMessage(errorMessage: String) {
                                                                    }

                                                                    override fun setErrorMessage(
                                                                        errorMessage: String,
                                                                        link: XDebuggerTreeNodeHyperlink?
                                                                    ) {
                                                                    }

                                                                    override fun setMessage(
                                                                        message: String,
                                                                        icon: Icon?,
                                                                        attributes: SimpleTextAttributes,
                                                                        link: XDebuggerTreeNodeHyperlink?
                                                                    ) {
                                                                    }

                                                                })

//                                                                children.getValue(0).computePresentation(object :
//                                                                    XValueNodePresentationConfigurator.ConfigurableXValueNodeImpl() {
//                                                                    override fun applyPresentation(
//                                                                        icon: Icon?,
//                                                                        valuePresenter: XValuePresentation,
//                                                                        hasChildren: Boolean
//                                                                    ) {
//                                                                        println("app : ${children.getValue(0)} ${valuePresenter.type}")
//                                                                    }
//
//                                                                    override fun setFullValueEvaluator(
//                                                                        fullValueEvaluator: XFullValueEvaluator
//                                                                    ) {
//
//                                                                    }
//
//                                                                }, XValuePlace.TOOLTIP)
                                                            } catch (e : Exception) {
                                                                println("looking for this exception : $e")
                                                            }
                                                        }
                                                    } catch (e: Exception) {
                                                        println("exc : $e")
                                                    }
                                                }

                                                override fun tooManyChildren(remaining: Int) {
                                                    println("tooManyChildren $remaining")
                                                }

                                                override fun setAlreadySorted(alreadySorted: Boolean) {
                                                    println("setAlreadySorted")
                                                }

                                                override fun setErrorMessage(errorMessage: String) {
                                                    println("setErrorMessage")
                                                }

                                                override fun setErrorMessage(
                                                    errorMessage: String,
                                                    link: XDebuggerTreeNodeHyperlink?
                                                ) {
                                                    println("setErrorMessage")
                                                }

                                                override fun setMessage(
                                                    message: String,
                                                    icon: Icon?,
                                                    attributes: SimpleTextAttributes,
                                                    link: XDebuggerTreeNodeHyperlink?
                                                ) {
                                                    println("setMessage")
                                                }

                                            })

//                                        result.computePresentation(object :
//                                            XValueNodePresentationConfigurator.ConfigurableXValueNodeImpl() {
//                                            override fun applyPresentation(
//                                                icon: Icon?,
//                                                valuePresenter: XValuePresentation,
//                                                hasChildren: Boolean
//                                            ) {
//                                                println("valuePresenter : $valuePresenter.")
//                                                println("valuePresenterGetType : ${valuePresenter.type}")
//                                                println("hasChildren : $hasChildren")
//
////                                                valuePresenter.renderValue(object : XValuePresentation.XValueTextRenderer {
////                                                    override fun renderValue(value: String) {
////                                                        println("renderValue $value")
////                                                    }
////
////                                                    override fun renderValue(value: String, key: TextAttributesKey) {
////                                                        println("renderValue $value, key $key")
////                                                    }
////
////                                                    override fun renderStringValue(value: String) {
////                                                        println("renderStringValue : $value")
////                                                    }
////
////                                                    override fun renderStringValue(
////                                                        value: String,
////                                                        additionalSpecialCharsToHighlight: String?,
////                                                        maxLength: Int
////                                                    ) {
////                                                        println("renderStringValue $value add $additionalSpecialCharsToHighlight")
////                                                    }
////
////                                                    override fun renderNumericValue(value: String) {
////                                                        println("renderNumericValue $value")
////                                                    }
////
////                                                    override fun renderKeywordValue(value: String) {
////                                                        println("renderKeywordValue $value")
////                                                    }
////
////                                                    override fun renderComment(comment: String) {
////                                                        println("renderComment $comment")
////                                                    }
////
////                                                    override fun renderSpecialSymbol(symbol: String) {
////                                                        println("renderSpecialSymbol $symbol")
////                                                    }
////
////                                                    override fun renderError(error: String) {
////                                                        println("renderError $error")
////                                                    }
////
////                                                })
//
//                                                val text = SimpleColoredText()
//                                                XValueNodeImpl.buildText(valuePresenter, text)
//
//                                                for (i in text.texts) {
//                                                    println("valuePresenter texts : $i")
//                                                }
//                                                val simpleColouredComp = HintUtil.createInformationComponent()
//                                                text.appendToComponent(simpleColouredComp)
//                                                content.add(simpleColouredComp)
//                                            }
//
//                                            override fun setFullValueEvaluator(fullValueEvaluator: XFullValueEvaluator) {
//                                                println("fullValueEvaluator : $fullValueEvaluator")
//                                            }
//
//                                        }, XValuePlace.TOOLTIP)
                                    }

                                }, xDebuggerManager.currentSession!!.currentPosition)

//                            if (susppendContext.activeExecutionStack?.topFrame != null) {
//
//                                println("support for code fragment or not : ${susppendContext.activeExecutionStack!!.topFrame!!.evaluator?.isCodeFragmentEvaluationSupported}")
//
//                                val evaluator = susppendContext.activeExecutionStack!!.topFrame?.evaluator
//
//                                susppendContext.activeExecutionStack!!.topFrame?.evaluator
//                                    ?.evaluate("aa", object : XDebuggerEvaluator.XEvaluationCallback {
//                                        override fun errorOccurred(errorMessage: String) {
//                                            println("errorMessage : $errorMessage")
//                                        }
//
//                                        override fun evaluated(result: XValue) {
//                                            println("result (XValue) : ${result}")
//                                            println("result (XValue) : smth : ${result}")
//                                        }
//
//                                    }, xDebuggerManager.currentSession!!.currentPosition)
//
//                            } else {
//                                println("active execution doesnt exist lmao")
//                            }
                        }

                        executor.shutdown()

                    }

                    override fun beforeSessionResume() {
                        super.beforeSessionResume()


                        val susppendContext : XSuspendContext = xDebuggerManager.currentSession!!.suspendContext


                        if (susppendContext.activeExecutionStack?.topFrame != null) {



//                            susppendContext.activeExecutionStack!!.topFrame?.evaluator
//                                ?.evaluate("aa", object : XDebuggerEvaluator.XEvaluationCallback {
//                                    override fun errorOccurred(errorMessage: String) {
//                                        println("errorMessage : $errorMessage")
//                                    }
//
//                                    override fun evaluated(result: XValue) {
////                                        println("result (XValue) : $result")
////                                        println("result (XValue) : smth : $result")
//                                        result
//                                    }
//
//                                }, xDebuggerManager.currentSession!!.currentPosition)

                        } else {
                            println("active execution doesnt exist lmao")
                        }


                    }

                    override fun sessionStopped() {
                        super.sessionStopped()
                        content.add(button1)
                        content.remove(currentDebugPosition)
                    }
                })

                content.remove(button1)
            }


//            for (i in xDebuggerManager.breakpointManager.allBreakpoints)
//                println(i.sourcePosition)
//            println("bool : ${xDebuggerManager.currentSession?.currentPosition?.line}")
//            Messages.showMessageDialog(
//                xDebuggerManager.currentSession?.project?.projectFile?.name, "", Messages.getInformationIcon()
//            )
        }
        content.add(button1)

    }

    fun dumpCor() {
        DebugProbes.dumpCoroutines()
    }

}