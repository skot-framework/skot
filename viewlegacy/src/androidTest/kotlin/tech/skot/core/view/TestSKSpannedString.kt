package tech.skot.core.view

import org.junit.Test
import tech.skot.core.components.SKBoxViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponent
import tech.skot.viewlegacy.test.R

class TestSKSpannedString : SKTestView() {


    @Test
    fun testViewPart() {

        val skSpan1 = SKSpannedStringViewProxy(
            listOf(
                SKSpan(
                    "début",
                    typeface = SKSpanFormat.Bold,
                    colored = SKSpanFormat.Colored(Color(R.color.red))
                ),
                SKSpan("milieu", colored = SKSpanFormat.Colored(Color(R.color.green))),
                SKSpan("fin", typeface = SKSpanFormat.Bold, sized = SKSpanFormat.Sized(2f)),
                SKSpan(
                    "lien",
                    sized = SKSpanFormat.Sized(1.4f),
                    clickable = SKSpanFormat.Clickable(toast("tap sur lien"))
                ),
                SKSpan(
                    "\navec font spéciale",
                    typeface = SKSpanFormat.WithFont(Font(R.font.dancing_script_regular))
                )
            )
        )

        val box = SKBoxViewProxy(
            asItemVertical = true,
            itemsInitial = listOf(
                skSpan1
            )
        )

        testComponent(box)

    }

    @Test
    fun completeTest() {

        val skSpan1 =
            skSpannedString {
                colored(Color(R.color.red)) {

                    clickable(toast("lien")) {
                        append("coucoulien")

                        bold {
                            append(" rouge")
 *                         }
 *                         scale(1.4f) {
 *                             colored(Color(R.color.green)) {
 *                                 append(" vert ")
 *                             }
 *                             append("re-rouge")
 *                         }
 *                         append("lien encore")
 *                     }
 *
 *                     append("plus lien")
  *                }
  *            }


        val box = SKBoxViewProxy(
            asItemVertical = true,
            itemsInitial = listOf(
                SKSpannedStringViewProxy(skSpan1)
            )
        )

        testComponent(box)

    }
}
