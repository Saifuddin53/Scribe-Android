/**
 * The input method (IME) for the English language keyboard.
 *
 * Copyright (C) 2024 Scribe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package be.scri.services

import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_NONE
import be.scri.R
import be.scri.databinding.KeyboardViewCommandOptionsBinding
import be.scri.helpers.KeyboardBase
import be.scri.views.KeyboardView

class EnglishKeyboardIME : GeneralKeyboardIME("English") {
    override fun getKeyboardLayoutXML(): Int =
        if (getEnablePeriodAndCommaABC()) {
            R.xml.keys_letters_english
        } else {
            R.xml.keys_letters_english_without_period_and_comma
        }

    override val keyboardLetters = 0
    override val keyboardSymbols = 1
    override val keyboardSymbolShift = 2

    override var keyboard: KeyboardBase? = null
    override var keyboardView: KeyboardView? = null
    override var lastShiftPressTS = 0L
    override var keyboardMode = keyboardLetters
    override var inputTypeClass = InputType.TYPE_CLASS_TEXT
    override var enterKeyType = IME_ACTION_NONE
    override var switchToLetters = false
    override var hasTextBeforeCursor = false
    override lateinit var binding: KeyboardViewCommandOptionsBinding

    override fun onCreateInputView(): View {
        binding = KeyboardViewCommandOptionsBinding.inflate(layoutInflater)
        setupCommandBarTheme(binding)
        val keyboardHolder = binding.root
        keyboardView = binding.keyboardView
        keyboardView!!.setKeyboard(keyboard!!)
        when (currentState) {
            ScribeState.IDLE -> keyboardView!!.setEnterKeyColor(null)
            else -> keyboardView!!.setEnterKeyColor(R.color.dark_scribe_blue)
        }
        keyboardView!!.setKeyboardHolder()
        keyboardView?.mOnKeyboardActionListener = this
        initializeEmojiButtons()
        updateUI()
        return keyboardHolder
    }

    override fun onKey(code: Int) {
        val inputConnection = currentInputConnection
        if (keyboard == null || inputConnection == null) {
            return
        }
        if (code != KeyboardBase.KEYCODE_SHIFT) {
            lastShiftPressTS = 0
        }

        when (code) {
            KeyboardBase.KEYCODE_DELETE -> {
                handleKeycodeDelete()
                keyboardView!!.invalidateAllKeys()
                disableAutoSuggest()
            }

            KeyboardBase.KEYCODE_SHIFT -> {
                super.handleKeyboardLetters(keyboardMode, keyboardView)
                keyboardView!!.invalidateAllKeys()
                disableAutoSuggest()
            }

            KeyboardBase.KEYCODE_ENTER -> {
                handleKeycodeEnter()
                disableAutoSuggest()
            }

            KeyboardBase.KEYCODE_MODE_CHANGE -> {
                handleModeChange(keyboardMode, keyboardView, this)
                disableAutoSuggest()
            }

            KeyboardBase.KEYCODE_SPACE -> {
                handleElseCondition(code, keyboardMode, binding = null)
                updateAutoSuggestText(nounTypeSuggestion)
            }

            else -> {
                if (currentState == ScribeState.IDLE || currentState == ScribeState.SELECT_COMMAND) {
                    handleElseCondition(code, keyboardMode, binding = null)
                    disableAutoSuggest()
                } else {
                    handleElseCondition(code, keyboardMode, keyboardBinding, commandBarState = true)
                    disableAutoSuggest()
                }
            }
        }

        lastWord = getLastWordBeforeCursor()
        Log.d("Debug", "$lastWord")
        autosuggestEmojis = findEmojisForLastWord(emojiKeywords, lastWord)
        nounTypeSuggestion = findNounTypeForLastWord(nounKeywords, lastWord)
        Log.d("Debug", "$autosuggestEmojis")
        Log.d("MY-TAG", "$nounTypeSuggestion")
        updateButtonText(isAutoSuggestEnabled, autosuggestEmojis)
        if (code != KeyboardBase.KEYCODE_SHIFT) {
            super.updateShiftKeyState()
        }
    }

    fun handleKeycodeDelete() {
        if (currentState == ScribeState.IDLE || currentState == ScribeState.SELECT_COMMAND) {
            handleDelete(false, keyboardBinding)
        } else {
            handleDelete(true, keyboardBinding)
        }
    }

    fun handleKeycodeEnter() {
        if (currentState == ScribeState.IDLE || currentState == ScribeState.SELECT_COMMAND) {
            handleKeycodeEnter(keyboardBinding, false)
        } else {
            handleKeycodeEnter(keyboardBinding, true)
            currentState = ScribeState.IDLE
            switchToCommandToolBar()
            updateUI()
        }
    }

    override fun onCreate() {
        super.onCreate()
        keyboard = KeyboardBase(this, getKeyboardLayoutXML(), enterKeyType)
        onCreateInputView()
        setupCommandBarTheme(binding)
    }
}
