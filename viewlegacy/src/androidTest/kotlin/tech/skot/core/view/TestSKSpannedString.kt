package tech.skot.core.view

import org.junit.Test
import tech.skot.core.components.SKBoxViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponent
import tech.skot.viewlegacy.test.R

class TestSKSpannedString : SKTestView() {


    fun spans(underline: Boolean, striked: Boolean): List<SKSpan> {
        return listOf(
            SKSpan(
                "début",
                typeface = SKSpanFormat.Bold,
                colored = SKSpanFormat.Colored(ColorRef(R.color.red)),
                underline = underline,
                striked = striked
            ),
            SKSpan(
                "milieu",
                colored = SKSpanFormat.Colored(ColorRef(R.color.green)),
                underline = underline,
                striked = striked
            ),
            SKSpan(
                "fin",
                typeface = SKSpanFormat.Bold,
                sized = SKSpanFormat.Sized(2f),
                underline = underline,
                striked = striked
            ),
            SKSpan(
                "lien",
                sized = SKSpanFormat.Sized(1.4f),
                clickable = SKSpanFormat.Clickable(toast("tap sur lien")),
                underline = underline,
                striked = striked
            ),
            SKSpan(
                "\navec font spéciale",
                typeface = SKSpanFormat.WithFont(Font(R.font.dancing_script_regular)),
                underline = underline,
                striked = striked
            )
        )
    }

    @Test
    fun testViewPart() {


        val skSpan = SKSpannedStringViewProxy(
            spans(false, false)
        )

        val skSpanUnderline = SKSpannedStringViewProxy(
            spans(true, false)
        )

        val skSpanStriked = SKSpannedStringViewProxy(
            spans(false, true)
        )

        val skSpanBoth = SKSpannedStringViewProxy(
            spans(true, true)
        )

        val skSpanAllInOne = SKSpannedStringViewProxy(
            spans(false, false) +
                    spans(true, false) +
                    spans(false, true) +
                    spans(true, true)
        )

        val box = SKBoxViewProxy(
            asItemVertical = true,
            itemsInitial = listOf(
                skSpan,
                skSpanUnderline,
                skSpanStriked,
                skSpanBoth,
                skSpanAllInOne
            )
        )

        testComponent(box)

    }

    @Test
    fun completeTest() {

        val skSpan1 =
            skSpannedString {
                colored(ColorRef(R.color.red)) {

                    clickable(toast("lien")) {
                        append("coucoulien")

                        bold {
                            append(" rouge")
                        }
                        scale(1.4f) {
                            colored(ColorRef(R.color.green)) {
                                append(" vert ")
                            }
                            append("re-rouge")
                        }
                        append("lien encore")
                    }

                    append("plus lien")
                }
            }


        val box = SKBoxViewProxy(
            asItemVertical = true,
            itemsInitial = listOf(
                SKSpannedStringViewProxy(skSpan1)
            )
        )

        testComponent(box)

    }
}