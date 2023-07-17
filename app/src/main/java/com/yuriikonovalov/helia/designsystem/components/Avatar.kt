package com.yuriikonovalov.helia.designsystem.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

enum class AvatarType {
    EDIT
}

@Composable
fun Avatar(
    uri: Uri?,
    onEditClick: () -> Unit,
    onDeletePhoto: () -> Unit,
    modifier: Modifier = Modifier,
    type: AvatarType = AvatarType.EDIT
) {

    Box(modifier = modifier) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(HeliaTheme.colors.greyscale400)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.ic_avatar),
                        contentDescription = null,
                        tint = HeliaTheme.colors.greyscale100
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(HeliaTheme.colors.greyscale400)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.ic_avatar),
                        contentDescription = null,
                        tint = HeliaTheme.colors.greyscale100
                    )
                }
            }
        )

        when (type) {
            AvatarType.EDIT -> EditButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onChangeClick = onEditClick,
                onDeleteClick = onDeletePhoto,
                photoNotNull = uri != null
            )
        }
    }
}

@Composable
private fun EditButton(
    photoNotNull: Boolean,
    onChangeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }

    Icon(
        modifier = modifier
            .size(25.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(role = Role.Button) {
                if (photoNotNull) {
                    isContextMenuVisible = true
                } else {
                    onChangeClick()
                }
            },
        painter = painterResource(R.drawable.ic_edit),
        contentDescription = stringResource(R.string.cd_avatar_change_a_photo_button),
        tint = HeliaTheme.colors.primary500
    )


    // provide a custom shape as the ExposedDropdownMenu uses a MaterialTheme extra small shape as a shape
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(extraSmall = HeliaTheme.shapes.extraSmall as CornerBasedShape),
        colorScheme = MaterialTheme.colorScheme.copy(surface = HeliaTheme.backgroundColor)
    ) {

        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.avatar_menu_change),
                        color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
                    )
                },
                onClick = {
                    onChangeClick()
                    isContextMenuVisible = false
                }
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.avatar_menu_delete),
                        color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
                    )
                },
                onClick = {
                    onDeleteClick()
                    isContextMenuVisible = false
                }
            )
        }
    }
}