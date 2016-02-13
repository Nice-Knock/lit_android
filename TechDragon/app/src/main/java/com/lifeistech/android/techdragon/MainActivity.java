package com.lifeistech.android.techdragon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    int power = 0,hp;
    TextView powerTextView;
    TextView hpTextView;
    TextView damageTextView;
    ImageView dragonImageV
    ProgressBar hpBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        powerTextView = (TextView)findViewById(R.id.powerTextView);
        damageTextView = (TextView)findViewById(R.id.damageTextView);
        dragonImageView = (ImageView)findViewById(R.id.dragonImageView);

        hp = 100;
        hpBar.setProgress(hp);
    }


    public void powerUp(View v) {
        /*
         *
		 * 問題1 画面上のTextViewの数字が3ずつ増えていくボタンを作ろう。
		 * 問題2 数字が10以上30未満ならば文字の色が緑に、30以上50未満ならば青、
		 * 50以上ならば赤になるコードを、if文を使って問題1の関数の中に加えてみよう。
		 * 問題3 問題2の条件分岐コードをswitch文で書き換えてみよう。
		 */
        power = power + 3;

        if(power >= 50){
            powerTextView.setTextColor(Color.RED);
        }else if(power >= 30){
            powerTextView.setTextColor(Color.BLUE);
        }else if(power >= 10){
            powerTextView.setTextColor(Color.GREEN);
        }

        powerTextView.setText(String.valueOf(power));



    }


    public void attack(View v) {
        /*
         *
		 * 問題4 攻撃ボタンを作って、押すと相手にダメージを与えるボタンを作ってみよう。
		 * 問題5 「damageAnimation」メソッドを呼び出すメソッドを書き加えてみよう。
		 * 問題6 相手の体力が0以下になったときに「clearAnimation」メソッドを書き加えてみよう。
		 */
        damageAnimation();
        hp = hp - power;
        hpBar.setProgress(hp);
        if(hp <= 0)clearAnimation();

    }


    public void retry(View v) {
        /*
		 * 
		 * 問題7 はじめからやり直す「リトライボタン」を作って、最初の状態に戻してみよう。
		 * やることは下にかいてあるよ！
		 */
        // 相手のHP
        hp = 100;
        hpTextView.setText(String.valueOf(hp));
        // 自分の攻撃力
        power = 0;
        powerTextView.setText(String.valueOf(power));
        // 相手モンスター復活
        dragonImageView.setVisibility(View.VISIBLE);
        // 攻撃力の文字の色を白に。(色が変わっている場合もあるので。)
        powerTextView.setTextColor(Color.WHITE);

    }


    public void info(View v) {
		/* 問題極 画面を遷移 */
        Intent intent = new Intent(this,infoActivity.class);
        startActivity(intent);

    }


    // 攻撃アニメーション
    public void damageAnimation() {
        if (power > 0) {
            damageTextView.setText(String.valueOf(power));
        } else {
            damageTextView.setText("miss");
        }
        AttackAnimation attackAnim = new AttackAnimation(
                this.getApplicationContext(),
                dragonImageView, damageTextView);
        dragonImageView.startAnimation(attackAnim);
    }



	// 消滅アニメーション
	public void clearAnimation() {
		AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f) ;
		alphaAnim.setDuration(1500) ;
		alphaAnim.setStartOffset(1500) ;
		alphaAnim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation anim) { }

			@Override
			public void onAnimationRepeat(Animation anim) { }

			@Override
			public void onAnimationEnd(Animation anim) {
				dragonImageView.setVisibility(View.INVISIBLE) ;
			}			
		} ) ;
		
		dragonImageView.startAnimation(alphaAnim) ;
	}


}
