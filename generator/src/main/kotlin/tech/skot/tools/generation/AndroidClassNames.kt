package tech.skot.tools.generation

import com.squareup.kotlinpoet.ClassName
import kotlinx.serialization.Serializable
import tech.skot.tools.generation.viewlegacy.coreComponentsPackage

object AndroidClassNames {
    val skActivity = ClassName(coreComponentsPackage, "SKActivity")
    val fragment = ClassName("androidx.fragment.app", "Fragment")
    val view = ClassName("android.view", "View")
    val context = ClassName("android.content", "Context")
    val layoutInflater = ClassName("android.view", "LayoutInflater")
    val frameLayout = ClassName("android.widget", "FrameLayout")
    val snackBar = ClassName("com.google.android.material.snackbar", "Snackbar")
    val build = ClassName("android.os", "Build")
    val viewGroup = ClassName("android.view", "ViewGroup")

}
