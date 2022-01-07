package tech.skot.core.components

class SKBoxViewMock(itemsInitial: List<SKComponentVC>, hiddenInitial: Boolean?): SKComponentViewMock(), SKBoxVC {
    override var items: List<SKComponentVC> = itemsInitial
    override var hidden: Boolean? = hiddenInitial
}