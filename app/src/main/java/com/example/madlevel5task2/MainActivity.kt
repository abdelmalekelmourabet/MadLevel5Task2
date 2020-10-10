package com.example.madlevel5task2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

const val ADD_GAME_REQUEST_CODE = 100

class MainActivity : AppCompatActivity() {

    private val gameArray = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(gameArray)
    private lateinit var viewModel: MainActivityViewModel
    private val savedGamesList = arrayListOf<Game>()
    private var delete = R.string.delete
    private var rollback = R.string.rollback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initViews()
        initViewModel()

        fab.setOnClickListener {
            startAddActivity()
        }
    }

    private fun startAddActivity() {
        val intent = Intent(this, AddActivity::class.java)
        startActivityForResult(intent, ADD_GAME_REQUEST_CODE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                savedGamesList.clear()
                savedGamesList.addAll(gameArray)
                viewModel.deleteAllGames()
                Snackbar.make(coordinatorLayout, delete, Snackbar.LENGTH_LONG)
                    .setAction(rollback) {
                        savedGamesList.forEach {
                            viewModel.addGame(it)
                        }
                    }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        rvGames.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvGames.adapter = gameAdapter
        createItemTouchHelper().attachToRecyclerView(rvGames)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        viewModel.games.observe(this, Observer { games ->
            this@MainActivity.gameArray.clear()
            this@MainActivity.gameArray.addAll(games)
            gameAdapter.notifyDataSetChanged()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_GAME_REQUEST_CODE -> {
                    val game = data!!.getParcelableExtra<Game>(EXTRA_GAME)
                    if (game != null) {
                        viewModel.addGame(game)
                    }
                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }


    private fun createItemTouchHelper(): ItemTouchHelper {

        //swipe to delete
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // move item
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deleteGame = gameArray[position]
                viewModel.deleteGame(deleteGame)
                Snackbar.make(coordinatorLayout, delete, Snackbar.LENGTH_LONG)
                    .setAction(rollback) {
                        viewModel.addGame(deleteGame)
                    }
                    .show()
            }
        }
        return ItemTouchHelper(callback)
    }
}
