package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

object SGIcons {

    @Composable
    fun Delete(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_delete), contentDescription = "", tint = MaterialTheme.colorScheme.error)
    }

    @Composable
    fun Bank(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.bank), contentDescription = "")

    }

    @Composable
    fun IsDeleted(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.delete_alert_outline), contentDescription = "")
    }

    @Composable
    fun Edit(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.file_edit_outline), contentDescription = "")
    }

    @Composable
    fun VisibilityOn(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_hide), contentDescription = "")
    }

    @Composable
    fun VisibilityOff(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_visibility_off), contentDescription = "")
    }

    @Composable
    fun Collapse(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_up), contentDescription = "")
    }

    @Composable
    fun Expand(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_icon_down), contentDescription = "")
    }

    @Composable
    fun Add(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_add), contentDescription = "", tint = tint)
    }

    @Composable
    fun Info(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_info), contentDescription = null)
    }

    @Composable
    fun Close(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_close), contentDescription = null)
    }

    @Composable
    fun ArrowBack(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_back), contentDescription = null)
    }

    @Composable
    fun Graph(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier , painter = painterResource(Res.drawable.ic_graph), contentDescription = null)
    }

    @Composable
    fun Calendar(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_calendar), contentDescription = null)
    }

    @Composable
    fun EmptyContent(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_empty_content), contentDescription = null)
    }

    @Composable
    fun Tick(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_correct), contentDescription = "")
    }

    @Composable
    fun Destination(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.icon_destination), contentDescription = null)
    }

    @Composable
    fun People(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.icon_people), contentDescription = null)
    }

    @Composable
    fun RightArrowThin(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_arrow_right_thin), contentDescription = null)
    }

    @Composable
    fun NextArrow(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_arrow_next), contentDescription = null)
    }

    @Composable
    fun EditIcon(
        modifier: Modifier = Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_edit), contentDescription = null)
    }

    @Composable
    fun LogoutIcon(
        modifier: Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_logout), contentDescription = null)
    }

    @Composable
    fun FilterIcon(
        modifier: Modifier
    ) {
        Icon(modifier = modifier, painter = painterResource(Res.drawable.ic_filter), contentDescription = null)
    }
}
