package com.websarva.wings.android.test.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.websarva.wings.android.test.DatabaseHelper
import com.websarva.wings.android.test.R
import com.websarva.wings.android.test.databinding.FragmentDashboardBinding
import com.websarva.wings.android.test.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

//    private lateinit var _helper: DatabaseHelper
//    private lateinit var homeViewModel: HomeViewModel
//    private var _binding: FragmentHomeBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
//
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        _helper = DatabaseHelper(requireActivity())
//
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }


    /**
     * 選択されたカクテルの主キーIDを表すプロパティ。
     */
    private var _cocktailId = -1
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
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _helper = DatabaseHelper(requireActivity())
//        val db = helper.writableDatabase
//        var sql = "SELECT account FROM Bank WHERE _id = 2 "
//        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
//        // カクテルリスト用ListView(lvCocktail)を取得。
//        val lvCocktail = view.findViewById<ListView>(R.id.lvCocktail)
//        // lvCocktailにリスナを登録。
//        lvCocktail.onItemClickListener = ListItemClickListener()
//        val listView: ListView = binding.lvCocktail
        binding.lvCocktail.onItemClickListener = ListItemClickListener()
//        binding.lvCocktail.text = DashboardViewModel.lvCocktail
//        binding.lvCocktail.setOnClickListener { ListItemClickListener() }
        binding.btnSave.setOnClickListener { onSaveButtonClick(root) }
//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
//        return view
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
//        // 感想欄を取得。
//        val etNote = view.findViewById<EditText>(R.id.etNote)
        // 入力された感想を取得。
        val note = binding.etNote.text.toString()

        // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
//        val helper = DatabaseHelper(requireActivity())
        val db = _helper.writableDatabase

        // まず、リストで選択されたカクテルのメモデータを削除。その後インサートを行う。
        // 削除用SQL文字列を用意。
        val sqlDelete = "DELETE FROM cocktailmemos WHERE _id = ?"
        // SQL文字列を元にプリペアドステートメントを取得。
        var stmt = db.compileStatement(sqlDelete)
        // 変数のバイド。
        stmt.bindLong(1, _cocktailId.toLong())
        // 削除SQLの実行。
        stmt.executeUpdateDelete()

        // インサート用SQL文字列の用意。
        val sqlInsert = "INSERT INTO cocktailmemos (_id, name, note) VALUES (?, ?, ?)"
        // SQL文字列を元にプリペアドステートメントを取得。
        stmt = db.compileStatement(sqlInsert)
        // 変数のバイド。
        stmt.bindLong(1, _cocktailId.toLong())
        stmt.bindString(2, _cocktailName)
        stmt.bindString(3, note)
        // インサートSQLの実行。
        stmt.executeInsert()

        // 感想欄の入力値を消去。
        binding.etNote.setText("")
//        // カクテル名を表示するTextViewを取得。
//        val tvCocktailName = view.findViewById<TextView>(R.id.tvCocktailName)
        // カクテル名を「未選択」に変更。
        binding.tvCocktailName.text = getString(R.string.tv_name)
        // 保存ボタンを取得。
//        val btnSave = view.findViewById<Button>(R.id.btnSave)
        // 保存ボタンをタップできないように変更。
        binding.btnSave.isEnabled = false
    }

    /**
     * リストがタップされたときの処理が記述されたメンバクラス。
     */
    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // タップされた行番号をプロパティの主キーIDに代入。
            _cocktailId = position
            // タップされた行のデータを取得。これがカクテル名となるので、プロパティに代入。
            _cocktailName = parent.getItemAtPosition(position) as String
            // カクテル名を表示するTextViewを取得。
//            val tvCocktailName = view.findViewById<TextView>(R.id.tvCocktailName)
            // カクテル名を表示するTextViewに表示カクテル名を設定。
            binding.tvCocktailName.text = _cocktailName

            // 保存ボタンを取得。
//            val btnSave = view.findViewById<Button>(R.id.btnSave)
            // 保存ボタンをタップできるように設定。
            binding.btnSave.isEnabled = true

            // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
//            val helper = DatabaseHelper(requireActivity())
            val db = _helper.writableDatabase
            // 主キーによる検索SQL文字列の用意。
            val sql = "SELECT * FROM cocktailmemos WHERE _id = ${_cocktailId}"
            // SQLの実行。
            val cursor = db.rawQuery(sql, null)

//            // 以下はバインド変数を使う場合の記述。
////			val sql = "SELECT * FROM cocktailmemos WHERE _id = ?"
////			val params = arrayOf(_cocktailId.toString())
////			val cursor = db.rawQuery(sql, params)
//
//            // 以下はSQL文を使わない場合。
////			val params = arrayOf(_cocktailId.toString())
////			val cursor = db.query("cocktailmemos", null, "_id = ?", params, null, null, null)

            // データベースから取得した値を格納する変数の用意。データがなかった時のための初期値も用意。
            var note = ""
            // SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得。
            while(cursor.moveToNext()) {
                // カラムのインデックス値を取得。
                val idxNote = cursor.getColumnIndex("note")
                // カラムのインデックス値を元に実際のデータを取得。
                note = cursor.getString(idxNote)
            }
            // 感想のEditTextの各画面部品を取得しデータベースの値を反映。
//            val etNote = findViewById<EditText>(R.id.etNote)
            binding.etNote.setText(note)
        }
    }

}