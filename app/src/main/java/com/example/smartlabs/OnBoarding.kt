package com.example.smartlabs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.smartlabs.Adapter.OnBoardingViewAdapter
import com.example.smartlabs.Model.OnBoardingItem
import com.example.smartlabs.databinding.ActivityOnBoardingBinding

class OnBoarding : AppCompatActivity() {

    // Объявляем переменную для использования биндинга
    private lateinit var binding: ActivityOnBoardingBinding
    // Список содержащий данные для приветсвенного экрана
    private val onBoardingItems = listOf<OnBoardingItem>(
        OnBoardingItem(  // Первый слайд
            image = R.drawable.first_slide_img,
            title = R.string.first_slide_title,
            description = R.string.first_slide_desc
        ),
        OnBoardingItem(  // Второй слайд
            image = R.drawable.second_slide_img,
            title = R.string.second_slide_title,
            description = R.string.second_slide_desc
        ),
        OnBoardingItem(  // Третий слайд
            image = R.drawable.third_slide_img,
            title = R.string.third_slide_title,
            description = R.string.third_slide_desc
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Даем переменной для биндинга значение
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Отображаем макет используя биндинг

        // Вызываем фукнцию отображения приветсвенного экрана
        setOnBoardingItems()
        // Вызываем функцию для отображения индикаторов
        setupIndicator()
        // Вызываем функцию для отображения конкретного индикатора
        setCurrentIndicator(0)

        // Настраиваем кнопку на завершение Приветсвенного экрана
        binding.skipAndFinishBtn.setOnClickListener{
            this.finish()
        }
    }

    private fun setOnBoardingItems() {  // Функция для отображение приветсвенного экрана
        // Объявляем переменную адаптера для отображения приветсвенного экрана
        val onBoardingViewAdapter = OnBoardingViewAdapter(onBoardingItems)
        // Назначаем адаптер на элемент отображения
        binding.onBoardingViewPager.adapter = onBoardingViewAdapter
        // Настраиваем обработчик события для переключения индикатора при смене слайда
        binding.onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            }
        )
        (binding.onBoardingViewPager.getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setupIndicator() {  // Функция для отображение необходимого количества индикаторов
        // Объявляем список индикаторов
        val indicators = arrayOfNulls<ImageView>(onBoardingItems.size)
        // Создаём параметры для отображения индикаторов
        val layoutParams : LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)

        // Перебераем список индикаторов
        for (i in indicators.indices) {
            //
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                // Присваиваем индикатору изображение
                it.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.onboardong_indicator_inactive)
                )
                // Присваиваем индикатору свойства элемента
                it.layoutParams = layoutParams
                // Отображаем индикатор
                binding.onBoardingIndicator.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {  // Функция для изменения вида активного и неактивного индикатора
        val childCount = binding.onBoardingIndicator.childCount  // Получение количества дочерних элементов, т.е. кол-во индикаторов
        for (i in 0 until childCount) {
            // Получаем конкретный индикатор для взаимодействия с ним
            val imageView = binding.onBoardingIndicator.getChildAt(i) as ImageView
            if (i == position) { // Если он соответсвует необходимой позиции, то меняем его на активный
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.onboardong_indicator_active)
                )
            } else { // Иначе меняем его на неактивный
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.onboardong_indicator_inactive)
                )
            }
        }

        // Проверяем являеться ли слайд последним, и если да, то меняем текст кнопки на "Завершить"
        if (position==childCount-1) binding.skipAndFinishBtn.text = binding.root.resources.getString(R.string.finish_btn)
        else binding.skipAndFinishBtn.text = binding.root.resources.getString(R.string.skip_btn)
    }
}