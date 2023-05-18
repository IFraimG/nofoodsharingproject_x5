package com.example.nofoodsharingproject.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nofoodsharingproject.adapters.FaqAdapter;
import com.example.nofoodsharingproject.databinding.ActivityFaqBinding;
import com.example.nofoodsharingproject.models.Faq;

import java.util.Arrays;

public class FaqActivity extends AppCompatActivity {
    private ActivityFaqBinding binding;
    private RecyclerView setterRecycler;
    private RecyclerView getterRecycler;
    private ImageView returnButton;

    private Faq[] getterQuesitons = new Faq[]{
            new Faq("Как создать объявление?", "Для создания объявления необходимо перейти на вкладку \"Объяления\", и проверить, что пункт \"Адрес магазина\" не пустой. Если же адреса нет, то вам необходимо прикрепиться к магазину. Читайте об этом ниже. Затем, необходимо нажать на кнопку \"Создать новое объявление\". В случае отсутствия данной кнопки, проверьте, есть ли у вас созданное активное объявление, и удалите его, посредством нажатия на кнопку \"Удалить объявление\", а затем нажмите  \"Создать новое объявление\". Затем, нажатием на названия продуктов, выберите необходимые вам товары. Помните, что их должно быть не более двух. В нижней части экрана отобразиться список товаров, выбранных вами. Также, не забудте ввести название вашего объявления. Затем, остаётся только нажать кнопку \"Готово\", и ваше объявление создано. Также, оно отобразится на экране \"Объявления\"."),
            new Faq("Каким образом я могу забрать продукты?", "Для того, чтобы забрать продукты из магазина необхдимо: подойти к кассам и спросить про выдачу заказов. В случае, если в магазине есть зона доставки, то обращаться стоит туда. Найдя человека, который готов выдать вам заказ, назовите 4х-символьный код в приложении, который отображается на экране \"Объявления\". После получения пакета с продуктами, вы обязательно должны нажать кнопку \"Забираю продукты\", показав это человеку, выдавшему вам продукты."),
            new Faq("В течении какого времени активно моё объявление?", "Объявление активно в течение трёх часов с момента его создания. В случае, если до закрытия самого магазина осталось менее установленного времени, объявление станет не активным автоматически при закрытии магазина."),
            new Faq("В течении какого времени можно забрать продукты?", "Продукты из магазина можно забрать не более чем через два часа с момента отклика отдающего пользователя на ваше объявление. "),
            new Faq("Как прикрепиться к магазину?", "Для прикрепления к магазину необходимо в нижней панели навигации выбрать фрагмент \"Карта\". В верхней части экрана отобразится окно, при нажатии на которое откроется список ближайших магазинов, участвующих в программе. Выбрав необходимый вам магазин, необходимо нажать кнопку \"Прикрепиться к этому магазину\"."),
            new Faq("Никто не откликается на мои объявления? Что делать?", "Возможно, ваши объявления слишком дорогие для \"отдающих\" пользователей. Советуем делать в объявлении всего один продукт: так, шанс того, что вам помогут увеличивается в разы. Также, проблема может быть в малом количестве \"Отдающих\" пользователей, прикреплённых к этому магазину. В таком случае, советуем прикрепиться к другому ближайшему магазину."),
            new Faq("Как я могу редактировать данные профиля?", "На вкладке \"Профиль\" вы можете редактировать свои данные. Для этого необходимо заполнить поля в соответствии с подсказками, а затем нажать на \"Редактировать профиль\". После этого, если все новые даннные введены корректно, профиль обновится."),

    };
    private Faq[] setterQuesitons = new Faq[]{
            new Faq("В течении какого времени можно забрать продукты?", "За 9 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов")
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFaqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setterRecycler = binding.faqSetterList;
        getterRecycler = binding.faqGetterList;
        returnButton = binding.faqReturn;

        returnButton.setOnClickListener(View -> finish());

        FaqAdapter faqAdapterSetter = new FaqAdapter(getApplicationContext());
        FaqAdapter faqAdapterGetter = new FaqAdapter(getApplicationContext());

        faqAdapterSetter.loadFaq(Arrays.asList(setterQuesitons));
        faqAdapterGetter.loadFaq(Arrays.asList(getterQuesitons));

        setterRecycler.setAdapter(faqAdapterSetter);
        getterRecycler.setAdapter(faqAdapterGetter);
    }
}
