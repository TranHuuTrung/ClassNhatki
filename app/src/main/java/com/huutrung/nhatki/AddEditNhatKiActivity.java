package com.huutrung.nhatki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.huutrung.nhatki.bean.NhatKi;

public class AddEditNhatKiActivity extends AppCompatActivity {

    NhatKi nhatki;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;

    private int mode;
    private EditText textTitle;
    private EditText textContent;

    private boolean needRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_nhat_ki);

        this.textTitle = (EditText) this.findViewById(R.id.edtTitle);
        this.textContent = (EditText) this.findViewById(R.id.edtContent);

        Intent intent = this.getIntent();
        this.nhatki = (NhatKi) intent.getSerializableExtra("nhatki");
        if (nhatki == null) {
            this.mode = MODE_CREATE;
        } else {
            this.mode = MODE_EDIT;
            this.textTitle.setText(nhatki.getNhatKiTitle());
            this.textContent.setText(nhatki.getNhatKiContent());
        }
    }

    // Người dùng Click vào nút Save.
    public void buttonSaveClicked(View view) {
        NhatKiDatabaseHelper db = new NhatKiDatabaseHelper(this);

        String title = this.textTitle.getText().toString();
        String content = this.textContent.getText().toString();

        if (title.equals("") || content.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter title & content", Toast.LENGTH_LONG).show();
            return;
        }
        if(mode == MODE_CREATE ) {
            this.nhatki= new NhatKi(title,content);
            db.addNhatKi(nhatki);
        } else  {
            this.nhatki.setNhatKiTitle(title);
            this.nhatki.setNhatKiContent(content);
            db.updateNhatKi(nhatki);
        }
        this.needRefresh = true;
        // Trở lại MainActivity.
        this.onBackPressed();
    }

    // Khi người dùng Click vào button Cancel.
    public void buttonCancelClicked(View view)  {
        // Không làm gì, trở về MainActivity.
        this.onBackPressed();
    }

    // Khi Activity này hoàn thành,
    // có thể cần gửi phản hồi gì đó về cho Activity đã gọi nó.
    @Override
    public void finish() {

        // Chuẩn bị dữ liệu Intent.
        Intent data = new Intent();
        // Yêu cầu MainActivity refresh lại ListView hoặc không.
        data.putExtra("needRefresh", needRefresh);

        // Activity đã hoàn thành OK, trả về dữ liệu.
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }

}
