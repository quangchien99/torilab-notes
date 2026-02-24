package torilab.assessment.notes.ui.screen.addeditnote.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import torilab.assessment.notes.ui.theme.ToriNotesTheme

@Composable
fun NoteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        singleLine = singleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                )
            }
            innerTextField()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteTextFieldEmptyPreview() {
    ToriNotesTheme(dynamicColor = false) {
        Column(modifier = Modifier.padding(16.dp)) {
            NoteTextField(
                value = "",
                onValueChange = {},
                placeholder = "Title",
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            NoteTextField(
                value = "",
                onValueChange = {},
                placeholder = "Start writing...",
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteTextFieldFilledPreview() {
    ToriNotesTheme(dynamicColor = false) {
        Column(modifier = Modifier.padding(16.dp)) {
            NoteTextField(
                value = "Meeting Notes",
                onValueChange = {},
                placeholder = "Title",
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            NoteTextField(
                value = "Discuss project timeline and assign tasks to team members.",
                onValueChange = {},
                placeholder = "Start writing...",
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}
