/*
 * Copyright 2003-2022 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package com.maddyhome.idea.vim.action

import com.intellij.codeInsight.hint.HintManagerImpl.ActionToIgnore
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PerformWithDocumentsCommitted
import com.intellij.openapi.actionSystem.PopupAction
import com.intellij.openapi.editor.EditorMouseHoverPopupManager
import com.intellij.openapi.project.DumbAware
import kotlin.reflect.jvm.javaMethod

/**
 * This action shows the editor's info tooltip, with both documentation and highlight info combined
 *
 * The platform only has the `QuickJavaDoc` and `ShowErrorDescription` actions, which display documentation and
 * highlight info respectively, but there isn't an action that shows both, only a tooltip handler.
 *
 * See [IDEA-249695](https://youtrack.jetbrains.com/issue/IDEA-249695) for more details
 *
 */
class ShowInfoTooltipAction: AnAction(), ActionToIgnore, DumbAware, PopupAction, PerformWithDocumentsCommitted {

  override fun getActionUpdateThread() = ActionUpdateThread.BGT

  override fun update(e: AnActionEvent) {
    val dataContext = e.dataContext
    val editor = CommonDataKeys.EDITOR.getData(dataContext)
    val element = CommonDataKeys.PSI_ELEMENT.getData(dataContext)
    if (editor == null || element == null) {
      e.presentation.isEnabled = false
    }
  }

  override fun actionPerformed(e: AnActionEvent) {
    val dataContext = e.dataContext
    val editor = CommonDataKeys.EDITOR.getData(dataContext) ?: return
    if (CommonDataKeys.PSI_ELEMENT.getData(dataContext) == null) return

    object : EditorMouseHoverPopupManager() {
      fun showInfoTooltip() {
        val context = createContext(editor, editor.caretModel.offset, 0)
        try {
          val method = ::scheduleProcessing.javaMethod
          method?.isAccessible = true
          method?.invoke(getInstance(), editor, context, true, true, true)
        } catch (e: Throwable) {
          // Do nothing
        }
      }
    }.showInfoTooltip()
  }
}