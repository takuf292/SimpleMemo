package jp.wings.nikkeibp.simplememo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateMemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);

        //ListActivityからインテントを取得
        Intent intent = this.getIntent();
        //値を取得
        String id = intent.getStringExtra("id");
        //画面に表示
        if(id.equals("")){
            //新規作成の場合
        } else {
            //編集の場合
        }

        /**
         * 登録ボタン処理
         */
        //　idがregisterのボタンを取得
        Button registerButton = (Button) findViewById(R.id.register);
        //　clickイベント追加
        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 入力内容を取得する
                EditText body  = (EditText)findViewById(R.id.body);
                String bodyStr = body.getText().toString();

                //　Todo:データベースに保存する

                // 保存後に一覧に戻る
                Intent intent = new Intent(CreateMemoActivity.this, jp.wings.nikkeibp.simplememo.ListActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 戻るボタン処理
         */
        //idがbackのボタンを取得
        Button backButton = (Button) findViewById(R.id.back);
        // Clickイベント追加
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存せずに一覧に戻る
                finish();
            }
        });

    }
}
