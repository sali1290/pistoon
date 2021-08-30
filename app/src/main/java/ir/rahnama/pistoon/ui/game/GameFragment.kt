package ir.rahnama.pistoon.ui.game

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment
import ir.rahnama.pistoon.webService.ApiClient
import ir.rahnama.pistoon.webService.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sqrt


class GameFragment : BaseFragment(),GameAdapter.SendDataToFragment {


    private lateinit var adapter: GameAdapter
    private lateinit var game_title:TextView
    private lateinit var game_record:TextView
    private lateinit var best_record:TextView
    private lateinit var car_seekbar:SeekBar
    private lateinit var recyclerView:RecyclerView
    private lateinit var back_state:ImageView
    private lateinit var translateAnimation:AnimationForGame
    //player
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var tonePlayer: MediaPlayer
    private lateinit var crashPlayer: MediaPlayer
    //starting game
    private lateinit var loading_layout:LinearLayout
    private lateinit var finishGame_layout:FrameLayout
    private lateinit var game_parent_layout:ConstraintLayout
   //on finish layout view
   private lateinit var game_record_onFinish_txt:TextView
    private lateinit var best_record_onFinish_txt:TextView
    private lateinit var btn_back_to_home:ConstraintLayout
    private lateinit var btn_start_game:ConstraintLayout
    private lateinit var txt_agin:TextView
   //volume button
   private lateinit var btn_volume_off_inGame:ToggleButton
   //pause btn
   private lateinit var btn_pauseGame:ImageView
    //enable or disable vilume
    private var volume = false



    //board pos
    private var position = 0
    private var seekbar = 0
    //user selection
    private var userposition = 0
    // user record counter
    private var record = 0
    //user wrong quest counter
    private var userWrongQuestion = 0
    //btn_again state - for resume game or restart game
    private var btn_again_state=false
    //backpress state
    private var backpressActive=false
    //finish game state when press back agiain exit game
    private var lastBackState=false

    //last animation method init
    private var mainLayoutHeight = 0
    private var animationSpeed=0




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)




       //loading layout
        loading_layout=view.findViewById(R.id.loading_layout)
        finishGame_layout=view.findViewById(R.id.finishGame_layout)
        game_parent_layout=view.findViewById(R.id.game_parent_layout)
        //onfinish game view
        game_record_onFinish_txt=view.findViewById(R.id.game_record_onFinish_txt)
        best_record_onFinish_txt=view.findViewById(R.id.game_best_record_onFinish_txt)
        btn_back_to_home=view.findViewById(R.id.btn_back_to_home)
        btn_start_game=view.findViewById(R.id.btn_start_game)
        txt_agin=view.findViewById(R.id.txt_agin)
        //volume btn
        btn_volume_off_inGame=view.findViewById(R.id.btn_volume_off_inGame)
        recyclerView = view.findViewById(R.id.game_recycler)
        back_state = view.findViewById(R.id.back_state)
        //seekbar init
         car_seekbar = view.findViewById(R.id.car_seekbar)
        car_seekbar.max=90
        //pause game btn
        btn_pauseGame = view.findViewById(R.id.btn_pauseGame)

        game_title = view.findViewById(R.id.game_title)
        game_record = view.findViewById(R.id.game_record)
        best_record = view.findViewById(R.id.last_record)
        //bottom init
        val bottom_navigation=requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottom_navigation.visibility=View.GONE
        //db init
        val db = MyDatabase(requireContext())



        Hawk.init(requireActivity()).build()




        //volume state
         volume =getvolumeSettingFromMemory()
        //volum btn state
        btn_volume_off_inGame.isChecked = volume
        btn_volume_off_inGame.setOnClickListener{

            if (getvolumeSettingFromMemory()){
                volumOff(false)
                volume =getvolumeSettingFromMemory()
                mediaPlayer.start()
            }else{
                volumOff(true)
                volume =getvolumeSettingFromMemory()
                mediaPlayer.stop()
            }
        }



        //get best record
        best_record.text=getBestRecordFromMemory().toString()


        //loading game
        Handler().postDelayed({ startingGame() }, 300)

        val resID = resources.getIdentifier("game_music_back", "raw", requireActivity().packageName)
        mediaPlayer = MediaPlayer.create(requireActivity(), resID)
        mediaPlayer.isLooping = true
        //beep
        val toneId = resources.getIdentifier(
            "get_current_answer_tone",
            "raw",
            requireActivity().packageName
        )
        tonePlayer = MediaPlayer.create(requireActivity(), toneId)
        tonePlayer.isLooping = false
        //crash
        val crashId = resources.getIdentifier("crash_tone", "raw", requireActivity().packageName)
        crashPlayer = MediaPlayer.create(requireActivity(), crashId)
        crashPlayer.isLooping=false


        //send board to adapter
        val Board_list = db.getAllBoard()
        Board_list.shuffle()
        adapter=GameAdapter(requireContext(), Board_list, this)
        recyclerView.adapter=adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)





        car_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                seekbar = p1
                when (p1) {
                    in 1..29 -> {
                        userposition = 3
                    }
                    in 30..59 -> {
                        userposition = 2
                    }
                    in 60..90 -> {
                        userposition = 1
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {
                when (seekbar) {
                    in 1..29 -> {
                        userposition = 3
                    }
                    in 30..59 -> {
                        userposition = 2
                    }
                    in 60..90 -> {
                        userposition = 1
                    }
                }
            }

        })



        btn_back_to_home.setOnClickListener{
            closeGameAndSaveData()
        }
        btn_start_game.setOnClickListener{
            if (btn_again_state){
                onResumeGame()
                btn_again_state=false
            }else{
                loadFragment(GameFragment())
            }
        }

        btn_pauseGame.setOnClickListener{
            btn_again_state=true
            onPauseGame()
        }

        return view
    }



    private fun closeGameAndSaveData(){
        saveRecordToMemory()
        requireActivity().supportFragmentManager.popBackStack()
        val fragmentManager: FragmentTransaction =requireActivity().supportFragmentManager.beginTransaction()
        fragmentManager.remove(this).commit()
    }

    //name of board to show user
    override fun sendData(name: String) {
        game_title.text=name
    }
    //current answer id
    override fun SendPostion(position: Int) {
      this.position=position
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    fun startingGame(){

        val fadeOut = ObjectAnimator.ofFloat(loading_layout, "alpha", 1f, 0f)
        fadeOut.duration=1200
        fadeOut.doOnEnd {
            loading_layout.background=requireContext().getDrawable(R.color.traffic_yellow)
            val fadeOut1 = ObjectAnimator.ofFloat(loading_layout, "alpha", 1f, 0f)
            fadeOut1.duration=1200
            fadeOut1.doOnEnd {
                loading_layout.background=requireContext().getDrawable(R.color.traffic_green)
                val fadeOut2 = ObjectAnimator.ofFloat(loading_layout, "alpha", 1f, 0f)
                fadeOut2.duration=1200
                fadeOut2.doOnEnd {
                    gameLaunched()
                    loading_layout.visibility=View.GONE
                    backpressActive=true
                }
                fadeOut2.start()
            }
            fadeOut1.doOnStart {  }
            fadeOut1.start()

        }
        fadeOut.doOnStart { loading_layout.visibility=View.VISIBLE }
        fadeOut.start()
    }




    @SuppressLint("UseCompatLoadingForDrawables")
    fun gameLaunched(){


        if (!volume) mediaPlayer.start()
        back_state.visibility=View.GONE

        mainLayoutHeight = game_parent_layout.height + 80
        val forSqrt = (mainLayoutHeight * 4 ).toFloat()
        animationSpeed =(mainLayoutHeight * 350 ) / 100
        val lastSpeed= animationSpeed / 2


        translateAnimation = AnimationForGame(0F, 0F, 0F, mainLayoutHeight.toFloat())
        translateAnimation.interpolator = LinearInterpolator()
        translateAnimation.duration = animationSpeed.toLong()
        translateAnimation.repeatMode=Animation.RESTART
        translateAnimation.repeatCount=Animation.INFINITE
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }
            override fun onAnimationEnd(animation: Animation) {

            }
            override fun onAnimationRepeat(animation: Animation) {

                if(animationSpeed <= lastSpeed){
                    translateAnimation.duration=lastSpeed.toLong()
                }else {
                    animationSpeed -= sqrt(forSqrt).toInt()
                    translateAnimation.duration= animationSpeed.toLong()
                }
                if (userposition==position){
                    if (!volume) tonePlayer.start()
                    record += 13
                    game_record.text=record.toString()
                }else{
                    userWrongQuestion ++
                    onFinishGame()
                }

               adapter.updateData()
            }
        })
        recyclerView.startAnimation(translateAnimation)


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun onFinishGame(){

        when(userWrongQuestion){
            1 -> car_seekbar.thumb = requireContext().getDrawable(R.drawable.car_injury_state_1)
            2 -> car_seekbar.thumb = requireContext().getDrawable(R.drawable.car_injury_state_2)
            3 -> {
                car_seekbar.thumb = requireContext().getDrawable(R.drawable.car_crash_thumb)
                if (!volume) crashPlayer.start()
                translateAnimation.cancel()
                game_title.text = ""
                back_state.visibility = View.VISIBLE
                mediaPlayer.stop()
                saveRecordToMemory()
                game_record_onFinish_txt.text = record.toString()
                best_record_onFinish_txt.text = getBestRecordFromMemory().toString()
                txt_agin.text = resources.getString(R.string.TryAgain_txt);
                loadFinishOrPausePage()
                lastBackState = true

            }
        }

    }


    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.remove(this)
        fragmentTransaction.commit()
    }


    private fun loadFinishOrPausePage(){
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.addTarget(R.id.finishGame_layout)
        transition.duration = 500
        TransitionManager.beginDelayedTransition(game_parent_layout, transition)
        finishGame_layout.visibility = View.VISIBLE
        car_seekbar.isEnabled=false
        car_seekbar.isClickable=false
    }

    private fun HideFinishOrPausePage(){
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.addTarget(R.id.finishGame_layout)
        transition.duration = 500
        TransitionManager.beginDelayedTransition(game_parent_layout, transition)
        finishGame_layout.visibility = View.GONE

    }

    private fun getvolumeSettingFromMemory() :Boolean{
        val sharedpref =requireActivity().getSharedPreferences("volumeOff", Context.MODE_PRIVATE)
        return sharedpref.getBoolean("volume", false)
    }

    private fun saveRecordToMemory(){
        val sharedpref =requireActivity().getSharedPreferences("gameRecord", Context.MODE_PRIVATE)
         if (sharedpref.getInt("record", 0)<record){
             SendRecordToServer()
             sharedpref.edit().putInt("record", record).apply()
         }
    }

    private fun getBestRecordFromMemory():Int{
        val sharedpref =requireActivity().getSharedPreferences("gameRecord", Context.MODE_PRIVATE)
        return sharedpref.getInt("record", 0)
    }

    private fun volumOff(volume: Boolean){
        val sharedpref =requireActivity().getSharedPreferences("volumeOff", Context.MODE_PRIVATE)
        sharedpref.edit().putBoolean("volume", volume).apply()
    }


    private fun onPauseGame(){
        game_record_onFinish_txt.text=record.toString()
        best_record_onFinish_txt.text=getBestRecordFromMemory().toString()
        back_state.visibility=View.VISIBLE
        txt_agin.text= resources.getString(R.string.edame);
        mediaPlayer.pause()
        loadFinishOrPausePage()
        translateAnimation.pause()
    }

    private fun onResumeGame(){
        HideFinishOrPausePage()
        mediaPlayer.start()
        back_state.visibility=View.GONE
        translateAnimation.resume()
        car_seekbar.isEnabled=true
        car_seekbar.isClickable=true
    }


    override fun onBackPressed(): Boolean {

      if (backpressActive){

          if (lastBackState){
              closeGameAndSaveData()
          }
          when(btn_again_state){
              false -> {
                  btn_again_state = true
                  onPauseGame()
              }
              true -> {
                  onResumeGame()
                  btn_again_state = false
              }
          }
      }
        return true
    }


    private fun getUserName(): String? {
        val sharedpref =requireActivity().getSharedPreferences("gameRecord", Context.MODE_PRIVATE)
        return sharedpref.getString("user_name","")
    }

    private fun SendRecordToServer(){
        val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
        val call : Call<Void> =apiService.UpdateGameRecord(getUserName()!!,record)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
    }




}