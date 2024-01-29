package com.bav.wbapp.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.bav.wbapp.databinding.InfoScreenBinding

class InfoScreen : Fragment() {

    private lateinit var binding: InfoScreenBinding

    companion object {
        private const val TITLE = "О приложении"
        private const val INFO =
            "Подписывайтесь\n и узнайте первыми о скидках, акциях и конкурсах повышения квалификации и труда красоты!"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = InfoScreenBinding.inflate(inflater, container, false)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                InfoView()
            }
        }

        return binding.root
    }

    @Composable
    private fun InfoView(modifier: Modifier = Modifier) {
        Column(modifier = modifier.fillMaxSize()) {
            InfoImageView(modifier)
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color.DarkGray)
            )
            InfoBoxView(modifier)
        }
    }

    @Composable
    private fun InfoBoxView(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = INFO,
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(start = 30.dp, top = 45.dp, end = 30.dp, bottom = 45.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Image(
                        painter = painterResource(id = com.bav.core.R.drawable.info_facebook_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(43.dp)
                    )
                    Image(
                        painter = painterResource(id = com.bav.core.R.drawable.info_vk_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(43.dp)
                    )
                    Image(
                        painter = painterResource(id = com.bav.core.R.drawable.info_tvitter_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(43.dp)
                    )
                    Image(
                        painter = painterResource(id = com.bav.core.R.drawable.info_instagramm_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(43.dp)
                    )
                    Image(
                        painter = painterResource(id = com.bav.core.R.drawable.info_odnoklassniki_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(43.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun InfoImageView(modifier: Modifier = Modifier) {
        Box(modifier = modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(com.bav.core.R.drawable.info_image),
                contentDescription = "",
                modifier = modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = TITLE.uppercase(),
                modifier = modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}