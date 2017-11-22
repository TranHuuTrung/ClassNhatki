package com.huutrung.nhatki;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.huutrung.nhatki.bean.NhatKi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;

    private final List<NhatKi> nhatkiList = new ArrayList<NhatKi>();
    private ArrayAdapter<NhatKi> listViewAdapter;


    private static final int MY_REQUEST_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listView);

        NhatKiDatabaseHelper db = new NhatKiDatabaseHelper(this);
        db.createDefaultNotesIfNeed();

        List<NhatKi> list=  db.getAllNhatKi();
        this.nhatkiList.addAll(list);

        // Định nghĩa một Adapter.
        // 1 - Context
        // 2 - Layout cho các dòng.
        // 3 - ID của TextView mà dữ liệu sẽ được ghi vào
        // 4 - Danh sách dữ liệu.

        this.listViewAdapter = new ArrayAdapter<NhatKi>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, this.nhatkiList);
        // Đăng ký Adapter cho ListView.
        this.listView.setAdapter(this.listViewAdapter);

        // Đăng ký Context menu cho ListView.
        registerForContextMenu(this.listView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_VIEW , 0, "View Nhat Ki");
        menu.add(0, MENU_ITEM_CREATE , 1, "Create Nhat Ki");
        menu.add(0, MENU_ITEM_EDIT , 2, "Edit Nhat Ki");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete Nhat Ki");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final NhatKi selectedNhatKi = (NhatKi) this.listView.getItemAtPosition(info.position);

        if(item.getItemId() == MENU_ITEM_VIEW){
            Toast.makeText(getApplicationContext(),selectedNhatKi.getNhatKiContent(),Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId() == MENU_ITEM_CREATE){
            Intent intent = new Intent(this, AddEditNhatKiActivity.class);
            // Start AddEditNoteActivity, có phản hồi.
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        }else if(item.getItemId() == MENU_ITEM_EDIT ){
            Intent intent = new Intent(this, AddEditNhatKiActivity.class);
            intent.putExtra("nhatki", selectedNhatKi);
            // Start AddEditNoteActivity, có phản hồi.
            this.startActivityForResult(intent,MY_REQUEST_CODE);
        }else if(item.getItemId() == MENU_ITEM_DELETE){
            // Hỏi trước khi xóa.
            new AlertDialog.Builder(this)
                    .setMessage(selectedNhatKi.getNhatKiTitle()+". Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteNhatKi(selectedNhatKi);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }else {
            return false;
        }
        return true;
    }
    // Người dùng đồng ý xóa một NhatKi.
    private void deleteNhatKi(NhatKi nhatki)  {
        NhatKiDatabaseHelper db = new NhatKiDatabaseHelper(this);
        db.deleteNhatKi(nhatki);
        this.nhatkiList.remove(nhatki);
        // Refresh ListView.
        this.listViewAdapter.notifyDataSetChanged();
    }

    // Khi AddEditNhatKiActivity hoàn thành, nó gửi phản hồi lại.
    // (Nếu bạn đã start nó bằng cách sử dụng startActivityForResult()  )
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE ) {
            boolean needRefresh = data.getBooleanExtra("needRefresh",true);
            // Refresh ListView
            if(needRefresh) {
                this.nhatkiList.clear();
                NhatKiDatabaseHelper db = new NhatKiDatabaseHelper(this);
                List<NhatKi> list=  db.getAllNhatKi();
                this.nhatkiList.addAll(list);
                // Thông báo dữ liệu thay đổi (Để refresh ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
