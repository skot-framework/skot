
import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.web.sugar.Web
import androidx.test.espresso.web.webdriver.DriverAtoms
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.reflect.KClass

abstract class Scenari<A : AppCompatActivity>(activityClass: KClass<A>, beforeActivityLaunched:(()->Unit)? = null) {

    @Rule
    @JvmField
    var rule = object :ActivityTestRule<A>(activityClass.java, true, true) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            beforeActivityLaunched?.invoke()
        }
    }

    @get:Rule
    val mGrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)


    @get:Rule
    val watcher = object : TestWatcher() {
        override fun failed(e: Throwable?, description: Description?) {
            super.failed(e, description)
            screenShot("error")
        }
    }

    fun string(strId: Int) = rule.activity.getString(strId)
    fun withHint(strId: Int) = onView(withHint(string(strId)))
    fun isEnabled(strId: Int) = onView(withText(string(strId))).check(matches(isEnabled()))
    fun isNotEnabled(strId: Int) = onView(withText(string(strId))).check(matches(not(isEnabled())))
    fun textDisplayed(text: String) = onView(withText(text)).check(matches(isDisplayed()))
    fun textNotDisplayed(text: String) = onView(withText(text)).check(matches(not(isDisplayed())))
    fun textDisplayed(strId: Int) = textDisplayed(string(strId))
    fun textNotDisplayed(strId: Int) = textNotDisplayed(string(strId))
    fun idDisplayed(id:Int) = onView(withId(id)).check(matches(isDisplayed()))
    fun idNotDisplayed(id:Int) = onView(withId(id)).check(matches(not(isDisplayed())))



    fun ViewInteraction.setText(newText: String) {
        try { perform(scrollTo()) } catch (ex: Exception) { }
        perform(click(), replaceText(newText), closeSoftKeyboard())
    }

    fun ViewInteraction.hasText(text: String) = check(matches(withText(text)))
    fun clickOn(textId: Int): ViewInteraction =
            clickOn(string(textId))

    fun clickOnId(viewId:Int) {
        onView(withId(viewId)).perform(click())
    }

    fun clickOn(text: String): ViewInteraction {

        return onView(withText(text)).apply {
            safeClick()
        }
    }

    fun ViewInteraction.safeClick() {
        try {
            perform(scrollTo())
        } catch (ex: Exception) {

        }
        perform(click())
    }

    fun back() {
        onView(isRoot()).perform(pressBack())
    }


    val activity: Activity?
        get() : Activity? {
            var currentActivity: Activity? = null

            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                resumedActivities.forEach {
                    if (resumedActivities.isNotEmpty()) {
                        currentActivity = it
                    }
                }
            }

            return currentActivity
        }

    abstract fun screenShot(name: String)


    fun Web.WebInteraction<*>.withId(id: String) =
            withElement(DriverAtoms.findElement(Locator.ID, id))

    fun Web.WebInteraction<*>.selectValue(selectId: String, value: String) =
            withElement(DriverAtoms.findElement(Locator.XPATH, "//select[@id='$selectId']//option[@value='$value']")).click()


    fun Web.WebInteraction<*>.withClass(className: String) =
            withElement(DriverAtoms.findElement(Locator.CLASS_NAME, className))

    fun Web.WebInteraction<*>.enterText(text: String) = perform(DriverAtoms.webKeys(text))

    fun Web.WebInteraction<*>.click() = perform(DriverAtoms.webClick())

}