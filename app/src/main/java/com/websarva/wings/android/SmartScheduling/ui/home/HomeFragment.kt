package com.websarva.wings.android.SmartScheduling.ui.home

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import com.websarva.wings.android.SmartScheduling.DatabaseHelper
import com.websarva.wings.android.SmartScheduling.R
import com.websarva.wings.android.SmartScheduling.databinding.ActivityMainBinding.inflate
import com.websarva.wings.android.SmartScheduling.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    /**
     * 選択されたカクテルの主キーIDを表すプロパティ。
     */
    private var _cocktailId = -1
    private var deleteId: Long = -1

    /**
     * 選択されたカクテル名を表すプロパティ。
     */
    private var lastview: View? = null

    private lateinit var _helper: DatabaseHelper

    //    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _helper = DatabaseHelper(requireActivity())

        val db = _helper.writableDatabase
        // 検索SQL文字列の用意。
        val sql = "SELECT * FROM cocktailmemos"
        // SQLの実行。
        val cursor = db.rawQuery(sql, null)

//        // SimpleAdapterで使用するMutableListオブジェクトを用意。
//        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()
        // SimpleAdapter第4引数from用データの用意。
        val from = arrayOf("date", "place", "genre")
        // SimpleAdapter第5引数to用データの用意。
        val to = intArrayOf(R.id.date_column, R.id.place_column, R.id.genre_column)
        // SimpleAdapterを生成。
        val adapter = SimpleCursorAdapter(requireActivity(), R.layout.row, cursor, from, to)
        // アダプタの登録。
        binding.lvMenu.adapter = adapter

        //リストリスナの登録
        binding.lvMenu.onItemLongClickListener = ListItemLongClickListener()
        binding.check.setOnClickListener { onDeleteButtonClick(root) }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        // ヘルパーオブジェクトの解放。
        _helper.close()
        super.onDestroy()
    }

//    /**
//     * オプションメニューの表示
//     */
////    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        // オプションメニュー用xmlファイルをインフレイト。
//        inflater.inflate(R.menu.delete_check, menu);
//        super.onCreateOptionsMenu(menu, inflater)
////        MenuInflater: inflater = getSupportMenuInflater();
////        MenuInflater.inflate(R.menu.delete_check, menu)
////        return true
//    }

//    /**
//     * オプションメニューの内容
//     */
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return (when(item.itemId) {
//            R.id.column_delete -> {
//                true
//            }
//            else ->
//                super.onOptionsItemSelected(item)
//        })
//    }
    /**
     * リストがタップされたときの処理が記述されたメンバクラス。
     */
    private inner class ListItemLongClickListener : AdapterView.OnItemLongClickListener {
        override fun onItemLongClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
            if (lastview != null) {
                lastview?.setBackgroundResource(R.color.white) }
            // タップされた行番号をプロパティの主キーIDに代入。
            _cocktailId = position
            // タップされた行のデータを取得。これがカクテル名となるので、プロパティに代入。
//            _cocktailName = parent.getItemAtPosition(position) as String
            //デバッグ用
//            println("LifeCycleSample" + id )
//            Toast.makeText(this, "${cocktailName}が現れた！", Toast.LENGTH_SHORT).show()
//            // カクテル名を表示するTextViewに表示カクテル名を設定。
//            binding.lvMenu.setBackgroundResource(R.color.white)
//            view.setBackgroundColor(0xfff7aec)
            view.setBackgroundResource(R.color.gray)
            if (lastview == view) {
                lastview?.setBackgroundResource(R.color.white) }
            lastview = view
//            println("LifeCycleSample" + color )
            // 保存ボタンをタップできるように設定。
            binding.check.isEnabled = true
            deleteId = id
    // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
//            val db = _helper.writableDatabase
//            // 主キーによる検索SQL文字列の用意。
////            val sql = "SELECT * FROM cocktailmemos WHERE _id = ${_cocktailId}"
//            val sql = "SELECT * FROM cocktailmemos"
//            // SQLの実行。
//            val cursor = db.rawQuery(sql, null)

//            // SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得。
//            while(cursor.moveToNext()) {
//                // カラムのインデックス値を取得。
//                val idxNote = cursor.getColumnIndex("note")
//                // カラムのインデックス値を元に実際のデータを取得。
//                note = cursor.getString(idxNote)
//            }
//            binding.etNote.setText(note)

            return true
        }
    }

    /**
     * 削除ボタンがタップされた時の処理メソッド。
     */
    fun onDeleteButtonClick(view: View) {
        // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
//        val helper = DatabaseHelper(requireActivity())
        val db = _helper.writableDatabase

        // まず、リストで選択されたカクテルのメモデータを削除。その後インサートを行う。
        // 削除用SQL文字列を用意。
        val sqlDelete = "DELETE FROM cocktailmemos WHERE _id = ${deleteId}"
        // SQL文字列を元にプリペアドステートメントを取得。
        var stmt = db.compileStatement(sqlDelete)
        // 変数のバイド。
//        stmt.bindLong(1, _id.toLong())
        // 削除SQLの実行。
        stmt.executeUpdateDelete()

        // 保存ボタンをタップできないように変更。
        binding.check.isEnabled = false
        onupdateView()
    }

     fun onupdateView() {
        val db = _helper.writableDatabase
        // 検索SQL文字列の用意。
        val sql = "SELECT * FROM cocktailmemos"
        // SQLの実行。
        val cursor = db.rawQuery(sql, null)

//        // SimpleAdapterで使用するMutableListオブジェクトを用意。
//        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()
        // SimpleAdapter第4引数from用データの用意。
        val from = arrayOf("date", "place", "genre")
        // SimpleAdapter第5引数to用データの用意。
        val to = intArrayOf(R.id.date_column, R.id.place_column, R.id.genre_column)
        // SimpleAdapterを生成。
        val adapter = SimpleCursorAdapter(requireActivity(), R.layout.row, cursor, from, to)
        // アダプタの登録。
        binding.lvMenu.adapter = adapter
    }
}
