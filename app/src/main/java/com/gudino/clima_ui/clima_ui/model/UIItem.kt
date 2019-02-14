package com.gudino.clima_ui.clima_ui.model

/**
 * UI item used to full the detail of a classified
 */
data class UIItem(val type: Int, val text: String? = "", val id: Int? = 0) {

    companion object {
        const val TEXT_TYPE = 0
        const val PLUS_BUTTON_TYPE = 1
        const val TITLE_TYPE = 2
        const val HEADER_TYPE = 3
    }
}