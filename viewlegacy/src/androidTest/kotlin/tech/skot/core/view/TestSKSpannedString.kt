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
                format = SKSpan.Format(
                    typeface = SKSpan.Bold,
                    color = ColorRef(R.color.red),
                    underline = underline,
                    striked = striked
                )
            ),
            SKSpan(
                "milieu",
                format = SKSpan.Format(
                    color = ColorRef(R.color.green),
                    underline = underline,
                    striked = striked
                )
            ),
            SKSpan(
                "fin",
                format = SKSpan.Format(
                    typeface = SKSpan.Bold,
                    scale = 2f,
                    underline = underline,
                    striked = striked
                )
            ),
            SKSpan(
                "lien",
                format = SKSpan.Format(
                    scale = 1.4f,
                    onTap = toast("tap sur lien"),
                    underline = underline,
                    striked = striked
                )
            ),
            SKSpan(
                "\navec font spéciale",
                format = SKSpan.Format(
                    typeface = SKSpan.WithFont(Font(R.font.dancing_script_regular)),
                    underline = underline,
                    striked = striked
                )
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



    @Test
    fun testBold() {

        val skSpan = skSpannedString {
            append("coucou ")
            bold {
                append("bold ")
            }
            append("normal")
        }

        val box = SKBoxViewProxy(
            asItemVertical = true,
            itemsInitial = listOf(
                SKSpannedStringViewProxy(skSpan)
            )
        )

        testComponent(box)
    }
}