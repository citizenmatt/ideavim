/*
 * Copyright 2003-2025 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package org.jetbrains.plugins.ideavim.ex.implementation.functions.commandLineFunctions

import com.maddyhome.idea.vim.api.injector
import com.maddyhome.idea.vim.ui.ex.ExEntryPanel
import org.jetbrains.plugins.ideavim.VimTestCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import kotlin.test.assertEquals

@Suppress("SpellCheckingInspection")
class GetCmdTypeFunctionTest : VimTestCase() {
  @BeforeEach
  override fun setUp(testInfo: TestInfo) {
    super.setUp(testInfo)
    configureByText("\n")
  }

  @Test
  fun `test getcmdtype() for a regular command`() {
    enterCommand("cmap <expr> z getcmdtype()")
    typeText(":fooz")
    assertRenderedExText("foo:")
  }

  @Test
  fun `test getcmdtype() for a forward search`() {
    enterCommand("cmap <expr> z getcmdtype()")
    typeText("/fooz")
    assertRenderedExText("foo/")
  }

  @Test
  fun `test getcmdtype() for a backward search`() {
    enterCommand("cmap <expr> z getcmdtype()")
    typeText("?fooz")
    assertRenderedExText("foo?")
  }

  @Test
  fun `test getcmdtype() for an expression command`() {
    enterCommand("cmap <expr> z getcmdtype()")
    typeText("i<C-r>=fooz")
    assertRenderedExText("foo=")
  }

  private fun assertRenderedExText(expected: String) {
    // Get the text directly from the text field. This DOES include prompts or rendered control characters
    assertEquals(expected, (injector.commandLine.getActiveCommandLine() as ExEntryPanel).getRenderedText())
  }
}
