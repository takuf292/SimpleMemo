package jp.wings.nikkeibp.simplememo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        //ToDo: データベースから値を取得する
        //仮のデータを作成
        ArrayList<HashMap<String, String>> tmpList = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            HashMap<String,String> data = new HashMap<>();
            //　引数には、（名前, 実際の値）という組み合わせで指定。名前はSimpleAdapterの引数で使う
            // body(サンプルデータを指定)
            data.put("body", "サンプルデータ"+i);
            // id(sampleId)を指定
            data.put("id","sampleId"+i);

            tmpList.add(data);
        }

        // Adapter生成
        // Todo:tmpListを正式なデータと入れ替える
        SimpleAdapter simpleAdapter = new SimpleAdapter(this
                , tmpList // 使用するデータ
                , android.R.layout.simple_expandable_list_item_2 //使用するレイアウト
                , new String[]{"body","id"} // どの項目を
                , new int[]{android.R.id.text1,android.R.id.text2}); // どのidの項目に入れるか

        // idがmemoListのListViewを取得
        ListView listView = (ListView) findViewById(R.id.memoList);
        listView.setAdapter(simpleAdapter);

        // リスト項目をクリックしたときの処理
        listView.setOnClickListener(new AdapterView.OnItemClickListener() {
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                // インテント作成　第二引数にはパッケージ名からの指定で、遷移先クラスを指定
                Intent intent = new Intent(ListActivity.this, jp.wings.nikkeibp.simplememo.CreateMemoActivity.class);

                //　選択されたビューを取得 TwoLineListItemを取得した後、text2の値を取得する
                TwoLineListItem two = (TwoLineListItem)view;
                TextView idTextView = (TextView)two.getText2();
                String isStr = (String) idTextView.getText();
                //　値を引き渡す（識別名、値）の順番で指定します (Intentを介して情報を受け渡している)
                intent.putExtra("id",isStr);
                // Activity起動
                startActivity(intent);
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
