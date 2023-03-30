/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package org.jetbrains.plugins.ideavim.action.change

import com.maddyhome.idea.vim.command.VimStateMachine
import com.maddyhome.idea.vim.group.IjOptions
import org.jetbrains.plugins.ideavim.VimTestCase
import org.junit.jupiter.api.Test

class UndoActionTest : VimTestCase() {
  @Test
  fun `test simple undo`() {
    val keys = listOf("dw", "u")
    val before = """
                A Discovery

                ${c}I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
    """.trimIndent()
    val after = before
    doTest(keys, before, after, VimStateMachine.Mode.COMMAND, VimStateMachine.SubMode.NONE)
    val editor = fixture.editor
    kotlin.test.assertFalse(editor.caretModel.primaryCaret.hasSelection())
  }

  // Not yet supported
  fun `undo after selection`() {
    val keys = listOf("v3eld", "u")
    val before = """
                A Discovery

                ${c}I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
    """.trimIndent()
    val after = before
    doTest(keys, before, after, VimStateMachine.Mode.COMMAND, VimStateMachine.SubMode.NONE)
    kotlin.test.assertFalse(hasSelection())
  }

  @Test
  fun `test undo with count`() {
    val keys = listOf("dwdwdw", "2u")
    val before = """
                A Discovery

                ${c}I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
    """.trimIndent()
    val after = """
                A Discovery

                ${c}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
    """.trimIndent()
    doTest(keys, before, after, VimStateMachine.Mode.COMMAND, VimStateMachine.SubMode.NONE)
    kotlin.test.assertFalse(hasSelection())
  }

  @Test
  fun `test cursor movements do not require additional undo`() {
    if (!optionsNoEditor().isSet(IjOptions.oldundo)) {
      val keys = listOf("a1<Esc>ea2<Esc>ea3<Esc>", "uu")
      val before = """
                A Discovery

                ${c}I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
      """.trimIndent()
      val after = """
                A Discovery

                I1 found$c it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
      """.trimIndent()
      doTest(keys, before, after, VimStateMachine.Mode.COMMAND, VimStateMachine.SubMode.NONE)
      kotlin.test.assertFalse(hasSelection())
    }
  }

  private fun hasSelection(): Boolean {
    val editor = fixture.editor
    return editor.caretModel.primaryCaret.hasSelection()
  }
}
