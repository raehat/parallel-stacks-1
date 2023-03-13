package com.example.myfirst

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.util.messages.MessageBusConnection
import com.intellij.xdebugger.*
import com.intellij.xdebugger.breakpoints.*
import com.intellij.xdebugger.frame.XSuspendContext
import com.intellij.xdebugger.impl.XDebugSessionImpl
import com.intellij.xdebugger.impl.breakpoints.XBreakpointBase
import org.jetbrains.annotations.NotNull
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel


class MyToolWindow : ToolWindowFactory, XBreakpointListener<XBreakpoint<*>> {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val content: JPanel = JPanel()
        toolWindow.contentManager.addContent(
            ContentFactory.SERVICE.getInstance().createContent(content, "", false)
        )

        val xDebuggerManager = XDebuggerManager.getInstance(project)
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

}