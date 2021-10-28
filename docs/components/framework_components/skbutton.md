# SKButton

Show a Button

## Usage

* In your ScreenVC add a SKButtonVC
  
  ```kotlin
  val myButtonId : SKButtonVC
  ```
  
  Launch the generation with the SKGenerate gradle task

* In your Screen, instantiate the SKButton
  
  ```kotlin
  override val myButtonId = SKButton(name) {//onTap}
  ```

* in your xml, add a Button / MaterialButton with id 'myButtonId'
