package com.websarva.wings.android.test.ui.dashboard

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import java.net.URLEncoder
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

        //リスナの登録
        binding.btnMapSearch.setOnClickListener { onMapSearchButtonClick(root) }
        binding.btnYelpSearch.setOnClickListener { onYelpSearchButtonClick(root) }
        binding.btnTabeloguSearch.setOnClickListener { onTabeloguSearchButtonClick(root) }
        binding.btnSave.setOnClickListener { onSaveButtonClick(root) }
        binding.date3Note.setOnClickListener { onDateButtonClick(root) }
//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val db = _helper.writableDatabase

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
     * 地図検索ボタンがタップされたときの処理メソッド。
     */
    fun onMapSearchButtonClick(view: View) {
        // 入力欄に入力されたキーワード文字列を取得。
        val SearchWord = binding.placeNote.text.toString()
        val SearchWord2 = binding.genreNote.text.toString()
        var searchWord ="${SearchWord} ${SearchWord2}".toString()
        // 入力されたキーワードをURLエンコード。
        searchWord = URLEncoder.encode(searchWord, "UTF-8")
        // マップアプリと連携するURI文字列を生成。
        val uriStr = "geo:0,0?q=${searchWord}"
        // URI文字列からURIオブジェクトを生成。
        val uri = Uri.parse(uriStr)
        // Intentオブジェクトを生成。
        val intent = Intent(Intent.ACTION_VIEW, uri)
        // アクティビティを起動。
        startActivity(intent)
        binding.place3Note.setText("${SearchWord}")
        binding.genre3Note.setText("${SearchWord2}")
    }

    /**
     * 地図検索ボタンがタップされたときの処理メソッド。
     */
    fun onYelpSearchButtonClick(view: View) {
        // 入力欄に入力されたキーワード文字列を取得。
        val SearchWord = binding.placeNote.text.toString()
        val SearchWord2 = binding.genreNote.text.toString()
        var searchWord ="${SearchWord} ${SearchWord2}".toString()
        // 入力されたキーワードをURLエンコード。
        searchWord = URLEncoder.encode(searchWord, "UTF-8")
        // マップアプリと連携するURI文字列を生成。
        val uriStr = "https://www.yelp.com/search?find_desc=${SearchWord} ${SearchWord2}&find_loc="
        // URI文字列からURIオブジェクトを生成。
        val uri = Uri.parse(uriStr)
        // Intentオブジェクトを生成。
        val intent = Intent(Intent.ACTION_VIEW, uri)
        // アクティビティを起動。
        startActivity(intent)
        binding.place3Note.setText("${SearchWord}")
        binding.genre3Note.setText("${SearchWord2}")
    }

    fun onTabeloguSearchButtonClick(view: View) {
        // マップアプリと連携するURI文字列を生成。
        val uriStr = "https://tabelog.com/"
        // URI文字列からURIオブジェクトを生成。
        val uri = Uri.parse(uriStr)
        // Intentオブジェクトを生成。
        val intent = Intent(Intent.ACTION_VIEW, uri)
        // アクティビティを起動。
        startActivity(intent)
    }

    fun onDateButtonClick(view: View) {
        val calendar = Calendar.getInstance()
        val Year = calendar.get(Calendar.YEAR)
        val Month = calendar.get(Calendar.MONTH)
        val Day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener() {view, year, month, dayOfMonth->
                binding.date3Note.text = "${year}/${month + 1}/${dayOfMonth}"
            },
            Year,
            Month,
            Day)
        datePickerDialog.show()
    }
}