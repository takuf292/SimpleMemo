package jp.wings.nikkeibp.simplememo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity  {

    // MemoOpenHelperクラスを定義
    MemoOpenHelper helper = null;

    //ダイアログの選択肢を格納する変数
    final String[] items = {"削除する", "編集する", "タイトル変更"};


    //viewリストの個別のIDにするための定数を　CreateMemoActivity の登録ボタン処理から分けてもらっている
    public static String pStr = null;

    //スマホの戻るボタンが押された時
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // データベースから値を取得する
        if(helper == null){
            helper = new MemoOpenHelper(ListActivity.this);
        }

        // メモリストデータを格納する変数
        final ArrayList<HashMap<String, String>> memoList = new ArrayList<>();
        // データベースを取得する
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            Cursor c = db.rawQuery("select uuid, body from MEMO_TABLE order by id", null);
            // Cursor(カーソル…SQLにおける処理の一つ　データベースの上から順に条件判断し、更新する)の先頭行があるかどうか確認
            boolean next = c.moveToFirst();

            // 取得したすべての行を取得
            while(next){
                HashMap<String,String> data = new HashMap<>();
                // 取得したカラムの順番（０から始まる）と型を指定してデータを取得する
                String uuid = c.getString(0);
                String body = c.getString(1);
                if(body.length() > 10){
                    // リストに表示するのは10文字まで
                    body = body.substring(0, 11) + "...";
                }
                // 引数には、（名前、実際の値）という組み合わせで指定します　名前はSimpleAdapterの引数で使用します
                data.put("body",body);
                data.put("id", uuid);
                memoList.add(data);
                //次の行が存在するか確認
                next = c.moveToNext();
            }
        } finally {
            // finallyは、tryの中で例外が発生したときでも必ず実行される
            // dbを開いたら確実にclose
            db.close();
        }



        // Adapter生成
        // Todo:tmpListを正式なデータと入れ替える
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this
                , memoList // 使用するデータ
                , android.R.layout.simple_expandable_list_item_2 //使用するレイアウト
                , new String[]{"body","id"} // どの項目を
                , new int[]{android.R.id.text1,android.R.id.text2}); // どのidの項目に入れるか
        // idがmemoListのListViewを取得
        ListView listView = (ListView) findViewById(R.id.memoList);
        listView.setAdapter(simpleAdapter);
        //viewにタグを追加して終わり




        // リスト項目をクリックしたときの処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                // インテント作成　第二引数にはパッケージ名からの指定で、遷移先クラスを指定
                Intent intent = new Intent(ListActivity.this, jp.wings.nikkeibp.simplememo.CreateMemoActivity.class);


                //　選択されたビューを取得 TwoLineListItemを取得した後、text2の値を取得する
                TwoLineListItem two = (TwoLineListItem)view;
                TextView idTextView = (TextView)two.getText2();

                String isStr = (String) idTextView.getText();
                String testId = (String) idTextView.getTag();
                //　値を引き渡す（識別名、値）の順番で指定します (Intentを介して情報を受け渡している)
                intent.putExtra("id",isStr);

                // Activity起動
                startActivity(intent);
            }

        });

        // リスト項目を長押しクリックした時の処理
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // 選択されたビューを取得 TwoLineListItemを取得した後、text2の値を取得する
                TwoLineListItem two = (TwoLineListItem)view;
                TextView idTextView = (TextView)two.getText2();
                final String idStr = (String) idTextView.getText();

                //アラートダイアログ実装
                final AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setTitle("選択してください");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //削除する　が選択された場合
                        if(which == 0){

                            // 長押しした項目をデータベースから削除
                            SQLiteDatabase db = helper.getWritableDatabase();
                            try {
                                db.execSQL("DELETE FROM MEMO_TABLE WHERE uuid = '"+ idStr +"'");
                            } finally {
                                db.close();
                            }
                            // 長押しした項目を画面から削除
                            memoList.remove(position);
                            simpleAdapter.notifyDataSetChanged();

                        } else if (which == 1){

                        } else {

                        }
                    }
                });
                builder.show();
                // trueにすることで通常のクリックイベントを発生させない
                return true;
            }
        });



        /**
         * 新規作成するボタン処理
         */
        // idがnewButtonのボタンを取得
        Button newButton = (Button) findViewById(R.id.newButton);
        // clickイベント追加
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CreateMemoActivity へ遷移
                Intent intent = new Intent(ListActivity.this,jp.wings.nikkeibp.simplememo.CreateMemoActivity.class);
                intent.putExtra("id","");
                startActivity(intent);
            }
        });
    }
}
