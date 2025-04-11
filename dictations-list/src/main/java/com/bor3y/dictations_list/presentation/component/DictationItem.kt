package com.bor3y.dictations_list.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bor3y.dictations_list.R

@Composable
fun DictationItem(
    modifier: Modifier = Modifier,
    title: String,
    isNew: Boolean,
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                onItemClick()
            }
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (isNew) {
            Text(
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.new_dictation),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                text = "New",
                color = Color.Black,
                fontSize = 14.sp
            )
        }
    }
}