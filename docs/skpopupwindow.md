# SKPopupWindow
Show a SKComponent as a popup with a view as an anchor

## Usage

* In your ScreenVC add a SKPopupWindowVC
```
@SKPassToParentView
val popupWindow : SKPopupWindow`
```
Voir [SKPassToParentView] (annotations.md)

* Launch the generation with the SKGenerate gradle task

* In your Screen, instantiate the popupWindow
* in the init of your ScreenView, set the anchor of the popupWindow
```
  init {
        popupWindowView.anchor = binding.toolBarButton
        popupWindowView.widthSize = ViewGroup.LayoutParams.MATCH_PARENT //default ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindowView.widthSize = ConstraintLayout.LayoutParams.WRAP_CONTENT //default ViewGroup.LayoutParams.WRAP_CONTENT
  }
```
* in your screen call
```
popupWindow.show(MyComponent({popupWindow.dismiss()}),behavior)
```
where behavior is one of :
SKWindowPopupVC.Cancelable(val onDismiss:(()->Unit)? = null) : click outside of popup dismiss it
SKWindowPopupVC.NotCancelable : click outside keep the popup open 

