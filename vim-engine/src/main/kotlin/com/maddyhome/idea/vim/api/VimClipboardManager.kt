/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package com.maddyhome.idea.vim.api

import com.maddyhome.idea.vim.common.TextRange
import java.awt.datatransfer.Transferable

/**
 * Interface representing a clipboard manager for the Vim text editor.
 * Vim supports two types of clipboards (or selections):
 * - **Primary**: This is a concept specific to Linux. It stores the most recently visually selected text and pastes its content on a middle mouse click.
 * - **Clipboard**: This is supported by all operating systems. It functions as a storage for the common 'copy and paste' operations typically done with Ctrl-C and Ctrl-V.
 */
interface VimClipboardManager {
  fun getPrimaryTextAndTransferableData(): Pair<String, List<Any>?>?
  /**
   * Returns the string currently on the system clipboard.
   *
   * @return The clipboard string or null if data isn't plain text
   */
  fun getClipboardTextAndTransferableData(): Pair<String, List<Any>?>?

  /**
   * Puts the supplied text into the system clipboard
   */
  fun setClipboardText(text: String, rawText: String = text, transferableData: List<Any>): Transferable?
  fun setPrimaryText(text: String, rawText: String = text, transferableData: List<Any>): Transferable?

  fun getTransferableData(vimEditor: VimEditor, textRange: TextRange, text: String): List<Any>

  fun preprocessText(
    vimEditor: VimEditor,
    textRange: TextRange,
    text: String,
    transferableData: List<*>,
  ): String
}
