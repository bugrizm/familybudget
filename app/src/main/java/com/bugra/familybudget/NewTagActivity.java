package com.bugra.familybudget;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bugra.familybudget.R;

import com.bugra.familybudget.entity.Tag;
import com.bugra.familybudget.entity.TagDTO;
import com.bugra.familybudget.http.InsertTagTask;
import com.bugra.familybudget.view.PaymentIconView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NewTagActivity extends ActionBarActivity {

    public static final int NEW_TAG_ACTIVITY_RESULT = 2;
    public static final String TAG_EXTRA = "TAG_EXTRA";
    private PaymentIconView selectedColor;

    private float maxTagLayoutWidth;
    private float scale;

    private Tag tag;

    @BindView(R.id.newTagLayout) RelativeLayout newTagLayout;
    @BindView(R.id.tagColorLinearLayout) LinearLayout tagColorLinearLayout;
    @BindView(R.id.textTagName) EditText tagNameText;
    @BindView(R.id.textTagIconText) EditText tagIconText;

    @BindString(R.string.ok) String strOk;
    @BindString(R.string.tag_name_cant_be_empty) String strTagNameCantBeEmpty;
    @BindString(R.string.tag_representation_letter_cant_be_empty) String strTagRepresentationLetterCantBeEmpty;
    @BindString(R.string.tag_color_should_be_chosen) String strTagColorShouldBeChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(TAG_EXTRA)) {
           tag = (Tag)getIntent().getSerializableExtra(TAG_EXTRA);
        }

        FrameLayout frameLayout = (FrameLayout) newTagLayout.getParent();
        frameLayout.measure(FrameLayout.LayoutParams.MATCH_PARENT, 0);
        maxTagLayoutWidth = frameLayout.getMeasuredWidth();
        scale = getResources().getDisplayMetrics().density;
        maxTagLayoutWidth = (maxTagLayoutWidth / 4) * scale + 0.5f;

        if(tag != null) {
            initFields();
        }
        initTagColors();
    }

    private void initFields() {
        tagNameText.setText(tag.getName());
        tagIconText.setText(tag.getIconText());
    }

    private void initTagColors() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.selectable_color_list, android.R.layout.simple_spinner_item);

        LinearLayout currentLinearLayout = null;

        for (int i=0; i<adapter.getCount(); i++) {
            CharSequence colorCharSequence = adapter.getItem(i);

            if(i%6 == 0) {
                currentLinearLayout = new LinearLayout(getApplicationContext());
                currentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                float calculatedHeight = 50 * scale + 0.5f;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int)calculatedHeight);

                currentLinearLayout.setLayoutParams(params);
                tagColorLinearLayout.addView(currentLinearLayout, params);
            }

            PaymentIconView paymentIconView = new PaymentIconView(this);

            paymentIconView.setColor(colorCharSequence.toString());

            if(tag!=null && tag.getColor().equals(paymentIconView.getColorHex())) {
                selectedColor = paymentIconView;
                paymentIconView.setTransparent(false);
            } else {
                paymentIconView.setTransparent(true);
            }

            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams((int)(50 * scale + 0.5f), (int)(50 * scale + 0.5f));

            paymentIconView.setLayoutParams(iconParams);
            paymentIconView.setOnClickListener(new PaymentIconViewOnClickListener());

            currentLinearLayout.addView(paymentIconView);
        }
    }


    public void saveButtonOnClick(View view) {
        TagDTO tagDTO = new TagDTO();
        String tagName = tagNameText.getText().toString().trim();
        String iconText = tagIconText.getText().toString().trim();

        if(tagName.equals("")) {
            tagNameText.setError(strTagNameCantBeEmpty);
            return;
        }
        if(iconText.equals("")) {
            tagIconText.setError(strTagRepresentationLetterCantBeEmpty);
            return;
        }
        if(selectedColor == null) {
            showErrorMessage(strTagColorShouldBeChosen);
            return;
        }

        tagDTO.setName(tagName);
        tagDTO.setIconText(iconText.charAt(0) + "");
        if(tag != null) {
            tagDTO.setId(tag.getId());
        }
        tagDTO.setColor(selectedColor.getColorHex());

        InsertTagTask insertTagTask = new InsertTagTask(this, tagDTO);
        insertTagTask.execute();
    }

    private void showErrorMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton(strOk, null).create().show();
    }

    class PaymentIconViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(((PaymentIconView)v).getTransparent()) {
                ((PaymentIconView)v).setTransparent(false);

                if(selectedColor != null) {
                    selectedColor.setTransparent(true);
                }
                selectedColor = (PaymentIconView) v;
            } else {
                ((PaymentIconView)v).setTransparent(true);
                selectedColor = null;
            }
        }
    }
}
