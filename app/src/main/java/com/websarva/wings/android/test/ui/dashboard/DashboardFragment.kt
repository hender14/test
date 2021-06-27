package com.websarva.wings.android.test.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.websarva.wings.android.test.DatabaseHelper
import com.websarva.wings.android.test.R
import com.websarva.wings.android.test.databinding.FragmentDashboardBinding
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class DashboardFragment : Fragment() {

    /**
     * 選択されたカクテルの主キーIDを表すプロパティ。
     */
    private var _Id = -1
    /**
     * 選択されたカクテル名を表すプロパティ。
     */
    private var _cocktailName = ""

//    var mContext: Context? = null
    //    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mContext = context
//    }
//    private val _helper = DatabaseHelper( mContext)
    private lateinit var _helper: DatabaseHelper
//    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _helper = DatabaseHelper(requireActivity())
//        //リストリスナの登録
//        binding.lvCocktail.onItemClickListener = ListItemClickListener()
        //リスナの登録
        binding.btnSave.setOnClickListener { onSaveButtonClick(root) }
//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val db = _helper.writableDatabase
        // 検索SQL文字列の用意。
        val sql = "SELECT * FROM cocktailmemos"
        // SQLの実行。
        val cursor = db.rawQuery(sql, null)

//        // SimpleAdapterで使用するMutableListオブジェクトを用意。
//        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()
        // SimpleAdapter第4引数from用データの用意。
        val from = arrayOf("_id", "date")
        // SimpleAdapter第5引数to用データの用意。
        val to = intArrayOf(android.R.id.text2, android.R.id.text2)
        // SimpleAdapterを生成。
        val adapter = SimpleCursorAdapter(requireActivity(), android.R.layout.simple_list_item_2, cursor, from, to)
        // アダプタの登録。
        binding.lvMenu.adapter = adapter

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

    /**
     * 保存ボタンがタップされた時の処理メソッド。
     */
    fun onSaveButtonClick(view: View) {
//    private inner class ButtonClickListener : View.OnClickListener {
//        override fun onClick(view: View) {

        val _Id = getToday()
        // 入力された日付、場所、ジャンル情報を取得。
        val date = binding.date3Note.text.toString()
        val place = binding.place3Note.text.toString()
        val genre = binding.genre3Note.text.toString()

        // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
//        val helper = DatabaseHelper(requireActivity())
        val db = _helper.writableDatabase

        // まず、リストで選択されたカクテルのメモデータを削除。その後インサートを行う。
        // 削除用SQL文字列を用意。
        val sqlDelete = "DELETE FROM cocktailmemos WHERE _id = ?"
        // SQL文字列を元にプリペアドステートメントを取得。
        var stmt = db.compileStatement(sqlDelete)
        // 変数のバイド。
        stmt.bindLong(1, _Id.toLong())
        // 削除SQLの実行。
        stmt.executeUpdateDelete()

        // インサート用SQL文字列の用意。
        val sqlInsert = "INSERT INTO cocktailmemos (_id, date, place, genre) VALUES (?, ?, ?, ?)"
        // SQL文字列を元にプリペアドステートメントを取得。
        stmt = db.compileStatement(sqlInsert)
        // 変数のバイド。
        stmt.bindLong(1, _Id.toLong())
        stmt.bindString(2, date)
        stmt.bindString(3, place)
        stmt.bindString(4, genre)
        // インサートSQLの実行。
        stmt.executeInsert()

            // 感想欄の入力値を消去。
            binding.date3Note.setText("")
            binding.place3Note.setText("")
            binding.genre3Note.setText("")
////            // 保存ボタンをタップできないように変更。
////            binding.btnSave.isEnabled = false
    }


    fun getToday(): String {
        val date = Date()
        val format = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return format.format(date)
    }


    /**
     * リストがタップされたときの処理が記述されたメンバクラス。
     */
//    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
//        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//            // タップされた行番号をプロパティの主キーIDに代入。
//            _cocktailId = position
//            // タップされた行のデータを取得。これがカクテル名となるので、プロパティに代入。
//            _cocktailName = parent.getItemAtPosition(position) as String
//            // カクテル名を表示するTextViewに表示カクテル名を設定。
//            binding.tvCocktailName.text = _cocktailName
//
//            // 保存ボタンをタップできるように設定。
//            binding.btnSave.isEnabled = true

            // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
//            val helper = DatabaseHelper(requireActivity())
//            val db = _helper.writableDatabase
//            // 主キーによる検索SQL文字列の用意。
//            val sql = "SELECT * FROM cocktailmemos WHERE _id = ${_cocktailId}"
//            val sql = "SELECT * FROM cocktailmemos"
//            // SQLの実行。
//            val cursor = db.rawQuery(sql, null)

            // 画面部品ListViewを取得
//            val lvMenu = binding.lvMenu.text.toString()
//            // SimpleAdapterで使用するMutableListオブジェクトを用意。
//            val menuList: MutableList<MutableMap<String, String>> = mutableListOf()
//
//            // SimpleAdapter第4引数from用データの用意。
//            val from = arrayOf("_id", "date")
//            // SimpleAdapter第5引数to用データの用意。
//            val to = intArrayOf(android.R.id.text1, android.R.id.text2)
//            // SimpleAdapterを生成。
//            val adapter = SimpleCursorAdapter(requireActivity(), android.R.layout.simple_list_item_2, cursor, from, to)
//            // アダプタの登録。
//            binding.lvMenu.adapter = adapter
            // リストタップのリスナクラス登録。
//            binding.lvMenu.onItemClickListener = ListItemClickListener()

//            // SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得。
//            while(cursor.moveToNext()) {
//                // カラムのインデックス値を取得。
//                val idxNote = cursor.getColumnIndex("note")
//                // カラムのインデックス値を元に実際のデータを取得。
//                note = cursor.getString(idxNote)
//            }
//            binding.etNote.setText(note)
//        }
//    }
//
}