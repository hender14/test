package com.websarva.wings.android.test.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.websarva.wings.android.test.DatabaseHelper
import com.websarva.wings.android.test.R
import com.websarva.wings.android.test.databinding.ActivityMainBinding.inflate
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
        val from = arrayOf("_id", "date", "place", "genre")
        // SimpleAdapter第5引数to用データの用意。
        val to = intArrayOf(R.id.id_column, R.id.date_column, R.id.place_column, R.id.genre_column)
        // SimpleAdapterを生成。
        val adapter = SimpleCursorAdapter(requireActivity(), R.layout.row, cursor, from, to)
        // アダプタの登録。
        binding.lvMenu.adapter = adapter

        //リストリスナの登録
        binding.lvMenu.onItemLongClickListener = ListItemLongClickListener()

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
            // タップされた行番号をプロパティの主キーIDに代入。
            _cocktailId = position
            // タップされた行のデータを取得。これがカクテル名となるので、プロパティに代入。
//            _cocktailName = parent.getItemAtPosition(position) as String
            //デバッグ用
            println("LifeCycleSample" + id )
//            Toast.makeText(this, "${cocktailName}が現れた！", Toast.LENGTH_SHORT).show()
//            // カクテル名を表示するTextViewに表示カクテル名を設定。
//            binding.lvMenu.text = _cocktailName
            view.setBackgroundColor(0xfff7aec)
            // 保存ボタンをタップできるように設定。
            binding.check.isEnabled = true

    // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
//            val helper = DatabaseHelper(requireActivity())
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

}
