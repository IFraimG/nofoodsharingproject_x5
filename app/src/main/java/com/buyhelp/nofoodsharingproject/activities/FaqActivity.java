package com.buyhelp.nofoodsharingproject.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.buyhelp.nofoodsharingproject.adapters.FaqAdapter;
import com.buyhelp.nofoodsharingproject.models.Faq;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.ActivityFaqBinding;

import java.util.Arrays;

public class FaqActivity extends AppCompatActivity {
    private ActivityFaqBinding binding;
    private AlertDialog alertDialog;

    private final Faq[] getterQuesitons = new Faq[]{
            new Faq("Как создать объявление?", "Для создания объявления необходимо перейти на вкладку \"Объяления\", и проверить, что пункт \"Адрес магазина\" не пустой. Если же адреса нет, то вам необходимо прикрепиться к магазину. Читайте об этом ниже. Затем, необходимо нажать на кнопку \"Создать новое объявление\". В случае отсутствия данной кнопки, проверьте, есть ли у вас созданное активное объявление, и удалите его, посредством нажатия на кнопку \"Удалить объявление\", а затем нажмите  \"Создать новое объявление\". Затем, нажатием на названия продуктов, выберите необходимые вам товары. Помните, что их должно быть не более двух. В нижней части экрана отобразиться список товаров, выбранных вами. Также, не забудте ввести название вашего объявления. Затем, остаётся только нажать кнопку \"Готово\", и ваше объявление создано. Также, оно отобразится на экране \"Объявления\"."),
            new Faq("Каким образом я могу забрать продукты?", "Для того, чтобы забрать продукты из магазина необхдимо: подойти к кассам и спросить про выдачу заказов. В случае, если в магазине есть зона доставки, то обращаться стоит туда. Найдя человека, который готов выдать вам заказ, назовите 4х-символьный код в приложении, который отображается на экране \"Объявления\". После получения пакета с продуктами, вы обязательно должны нажать кнопку \"Забираю продукты\", показав это человеку, выдавшему вам продукты."),
            new Faq("В течении какого времени активно моё объявление?", "Объявление активно в течение трёх часов с момента его создания. В случае, если до закрытия самого магазина осталось менее установленного времени, объявление станет не активным автоматически при закрытии магазина."),
            new Faq("В течении какого времени можно забрать продукты?", "Продукты из магазина можно забрать не более чем через два часа с момента отклика отдающего пользователя на ваше объявление. "),
            new Faq("Как прикрепиться к магазину?", "Для прикрепления к магазину необходимо в нижней панели навигации выбрать фрагмент \"Карта\". В верхней части экрана отобразится окно, при нажатии на которое откроется список ближайших магазинов, участвующих в программе. Выбрав необходимый вам магазин, необходимо нажать кнопку \"Прикрепиться к этому магазину\"."),
            new Faq("Никто не откликается на мои объявления? Что делать?", "Возможно, ваши объявления слишком дорогие для \"отдающих\" пользователей. Советуем делать в объявлении всего один продукт: так, шанс того, что вам помогут увеличивается в разы. Также, проблема может быть в малом количестве \"Отдающих\" пользователей, прикреплённых к этому магазину. В таком случае, советуем прикрепиться к другому ближайшему магазину."),
            new Faq("Как я могу редактировать данные профиля?", "На вкладке \"Профиль\" вы можете редактировать свои данные. Для этого необходимо заполнить поля в соответствии с подсказками, а затем нажать на \"Редактировать профиль\". После этого, если все новые даннные введены корректно, профиль обновится."),
    };
    private final Faq[] setterQuesitons = new Faq[]{
            new Faq("Я живу рядом с магазином и мне постоянно приходят уведомления. Могу ли я их отключить?", "Для того, чтобы отключить уведомления, необходимо: на экране \"Профиль\" в левом верхнем углу найти три точки. Нажав на них, выберите пункт \"Редактировние профиля\""),
            new Faq("Как прикрепиться к магазину?", "Для прикрепления к магазину необходимо в нижней панели навигации выбрать фрагмент \"Карта\". В верхней части экрана отобразится окно, при нажатии на которое откроется список ближайших магазинов, участвующих в программе. Выбрав необходимый вам магазин, необходимо нажать кнопку \"Прикрепиться к этому магазину\"."),
            new Faq("Хочу изменить данные моего профиля. Что делать?", "Перейдите в раздел \"Профиль\" и нажмите на три точки в левом верхнем углу. Нажмите\"Редактировать профиль\" и редактируйте ваш профиль."),
            new Faq("Как и кому мне передать продукты?", "После совершения покупки продуктов для принимающего пользователя, попросите кассира или сборщика заказов забрать эти продукты и отложить для нуждающегося пользователя, учавствующего в проекте \"Покупай - помогай\". Также, назовите сотуднику магазина 4х значный код, который будет сгенерирован в приложении."),
            new Faq("Решил помочь нуждающемуся, но не знаю что мне нужно для этого сделать.", "Для того,чтобы помочь нуждаемуся необходимо: либо на странице \"Объявления\" перейти в любое из представленных объявлений, либо просто, находясь в 75м от магазина перейти по пуш eведомлению, которое придёт вам на телефон, если в этом магазинне есть активое объявление нуждающегося. Затем, покупаете необходмые продукты из оьъявления, и передаёте их сотрудникам магазина. Более подробно с дальнейшими шагами моожете ознакомится в пункте выше")
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFaqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.faqReturn.setOnClickListener(View -> finish());

        FaqAdapter faqAdapterSetter = new FaqAdapter(getApplicationContext());
        FaqAdapter faqAdapterGetter = new FaqAdapter(getApplicationContext());

        faqAdapterSetter.loadFaq(Arrays.asList(setterQuesitons));
        faqAdapterGetter.loadFaq(Arrays.asList(getterQuesitons));

        binding.faqSetterList.setAdapter(faqAdapterSetter);
        binding.faqGetterList.setAdapter(faqAdapterGetter);

        binding.getterPolicyOpen.setOnClickListener(View -> createPrivacyPolicyDialog());
        binding.faqItemQuestion.setOnClickListener(View -> createFaqItemQuestionDialog());
    }

    private void createFaqItemQuestionDialog() {
        if (alertDialog != null && alertDialog.isShowing()) alertDialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        android.view.View customView = inflater.inflate(R.layout.fragment_faq_question_images, null);
        builder.setView(customView);

        alertDialog = builder.create();
        ImageView img1 = customView.findViewById(R.id.faq_image_load_1);
        ImageView img2 = customView.findViewById(R.id.faq_image_load_2);
        ImageView img3 = customView.findViewById(R.id.faq_image_load_3);
        ImageView img4 = customView.findViewById(R.id.faq_image_load_4);
        ImageView img5 = customView.findViewById(R.id.faq_image_load_5);
        ImageView img6 = customView.findViewById(R.id.faq_image_load_6);
        ImageView img7 = customView.findViewById(R.id.faq_image_load_7);

        Glide.with(alertDialog.getContext()).load(R.drawable.faq6).into(img1);
        Glide.with(alertDialog.getContext()).load(R.drawable.faq5).into(img2);
        Glide.with(alertDialog.getContext()).load(R.drawable.faq4).into(img3);
        Glide.with(alertDialog.getContext()).load(R.drawable.faq3).into(img4);
        Glide.with(alertDialog.getContext()).load(R.drawable.faq2).into(img5);
        Glide.with(alertDialog.getContext()).load(R.drawable.faq1).into(img6);
        Glide.with(alertDialog.getContext()).load(R.drawable.faq7).into(img7);
        alertDialog.show();
    }

    private void createPrivacyPolicyDialog() {
        if (alertDialog != null && alertDialog.isShowing()) alertDialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        android.view.View customView = inflater.inflate(R.layout.fragment_privacy_policy, null);
        builder.setView(customView);

        alertDialog = builder.create();
        alertDialog.show();
    }
}
