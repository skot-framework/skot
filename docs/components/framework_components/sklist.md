# SKList

Show a List or a grid

## Usage

* In your ScreenVC add a SKListVC

  ```kotlin
  val myList : SKListVC
  ```

  Launch the generation with the SKGenerate gradle task

* In your Screen, instantiate the SKList

  ```kotlin
  override val myList = SKList(name)
  ```

* Instantiate a SKComponent for each items you ant to show in your SKList, and add this list in your SKlist : 
  ```kotlin
  init{
      val listItems = listOf(
        MyComponentHeader("testHeader1"),
        MyComponent("test1"),
        MyComponent("test2"),
        MyComponentHeader("testHeader2"),
        MyComponent("test3"),
        MyComponent("test4"),
        MyComponent("test5"),
  )
  }
  ```

### Android
* in your xml, add a RecyclerView with your id (myList in this sample)

## Usage ++

