package com.github.rmitsubayashi.slackrighttodisconnect.settings

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.github.rmitsubayashi.domain.model.MessageTemplate
import kotlin.math.max

class MessageTemplateEditText
    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(RemoveLateTimeTextWatcher())
    }

    private var prevSelection = 0
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        // actual selection. we want cursor position change
        if (selStart != selEnd) {
            super.onSelectionChanged(selStart, selEnd)
            prevSelection = selStart
            return
        }
        val placeHolderRanges = getPlaceHolderRanges()
        for (range in placeHolderRanges) {
            if (selStart in range.first+1 until range.last) {
                if (prevSelection > selStart)
                    setSelection(range.first)
                else
                    setSelection(range.last)
                prevSelection = selStart
                return
            }
        }
        super.onSelectionChanged(selStart, selEnd)
        prevSelection = selStart
    }

    fun addLateTime() {
        val currentText = text.toString()
        val currentPos = selectionStart
        val newString = if (selectionStart == selectionEnd) {
            currentText.substring(0, selectionStart) +
                    MessageTemplate.LATE_TIME_PLACEHOLDER +
                    currentText.substring(selectionStart)
        } else {
            currentText.replaceRange(IntRange(selectionStart, selectionEnd-1), MessageTemplate.LATE_TIME_PLACEHOLDER)
        }
        setText(newString)
        val newPos = currentPos + MessageTemplate.LATE_TIME_PLACEHOLDER.length-1
        setSelection(newPos)
        prevSelection = newPos
    }

    private fun getPlaceHolderRanges(): List<IntRange> {
        val regex = MessageTemplate.LATE_TIME_PLACEHOLDER.toRegex()
        val currentText = text.toString()
        val matchResult = regex.findAll(currentText)
        val result = mutableListOf<IntRange>()
        matchResult.toList().map { result.add(IntRange(it.range.first, it.range.last+1)) }
        return result
    }

    inner class RemoveLateTimeTextWatcher: TextWatcher {
        private var prevString: String = ""
        private var removedPlaceHolder = false

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (count == 0) {
                val rangeStart = max(0, start-MessageTemplate.LATE_TIME_PLACEHOLDER.length+1)
                val placeholderOneLetterRemoved =
                    MessageTemplate.LATE_TIME_PLACEHOLDER.substring(0, MessageTemplate.LATE_TIME_PLACEHOLDER.length-1)
                if (s.substring(rangeStart, start) == placeholderOneLetterRemoved) {
                    val removeAllPlaceholder = s.toString().replace(placeholderOneLetterRemoved, "")
                    prevString = removeAllPlaceholder
                    setText(removeAllPlaceholder)
                    setSelection(rangeStart)
                    removedPlaceHolder = true
                }
            }
        }

        override fun afterTextChanged(s: Editable) {
            if (removedPlaceHolder) {
                removedPlaceHolder = false
                return
            }
            highlightPlaceholder(s.toString())
        }

        private fun highlightPlaceholder(newText: String) {
            if (newText.isEmpty() || newText == prevString) {
                return
            }
            val stringBuilder = SpannableStringBuilder(newText)
            val placeholderRanges = getPlaceHolderRanges()
            for (range in placeholderRanges) {
                val colorSpan = ForegroundColorSpan(Color.BLUE)
                stringBuilder.setSpan(colorSpan, range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            prevString = newText
            val selection = selectionStart
            text = stringBuilder
            setSelection(selection)
        }
    }
}