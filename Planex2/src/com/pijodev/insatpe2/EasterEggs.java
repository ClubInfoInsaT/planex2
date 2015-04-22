package com.pijodev.insatpe2;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EasterEggs {
	/** Easter Egg n°1 **/
	static void ee1() {
		// pull ohohoho ! 
	}
	
	/** Easter Egg n°2 **/// #
	private static int Oo=0;
	static void ee2(final Context o) {
		String OoO=" t", OOo="r", ooO="ue", oOo="is", Ooo="in", oO[]={
			"T"+OOo+"op, c'est"+OoO+OOo+"op"+OoO.substring(0, 1)+"!","I"+Ooo.substring(1)+"utile d'"+
			Ooo+oOo.substring(1)+oOo+OoO.substring(1)+"e"+OOo+"...","Tu y"+OoO+"ie"+Ooo.substring(1)+
			oOo.substring(1)+OoO+"a"+Ooo.substring(1)+"t q"+ooO+" ça"+OoO.substring(0, 1)+"?","Si"+
			OoO+"u me"+OoO.substring(0, 1)+"che"+OOo+"che"+oOo.substring(1)+","+OoO+"u me"+OoO+OOo+
			"ouve"+oOo.substring(1)+".","De"+OOo+Ooo.substring(1)+"ie"+OOo+" ave"+OOo+OoO.substring(1)+
			oOo+"seme"+Ooo.substring(1)+"t...","Tu l'au"+OOo+"a"+oOo.substring(1)+" voulu."+
			OoO.substring(0, 1)+"Que"+OoO.substring(0, 1)+"le d"+ooO+"l comme"+Ooo.substring(1)+"ce !"
		};
		MyToast.show(o, oO[Oo], 0);
		if(Oo == oO.length-1)
			i2(o);
		else
			Oo++;
	}
	private static LinearLayout OOooO;
	private static TextView OooOO;
	private static int OOoOO;
	private static void i2(final Context o) {
		OOooO = new LinearLayout(o);
		OOooO.addView(OooOO = new TextView(o));String OooOo="\t\t\t\t";
		OooOO.setText(OooOo+"=== \u004Do\u0072\u0070i\u006f\u006E ==="+OooOo);
		OooOO.setGravity(1);
		OOooO.addView(OooOO = new TextView(o));
		OooOO.setGravity(1);
		OOooO.addView(new View(o));
		OOooO.setOrientation(1);
		OOooO.setGravity(1);
		Button OOOoo=new Button(o);OOOoo.setText("Réessayer");
		final int[][] oOoo = new int[3][];
		OOoOO = 1;
		OOOoo.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				oOoo[0]=null;
				f2(o, oOoo);
			}});
		OOooO.addView(OOOoo);
		new Builder(o).setView(OOooO).show();
		f2(o, oOoo);
	}
	private static void f2(Context oOoO, int[][] OoO) {
		boolean oO=false;
		String oOO="Niveau : " + (OOoOO==3?"impossible":"facile");
		if(OoO[0]==null) {
			OoO[0]=new int[]{0,0,0};OoO[1]=new int[]{0,0,0};OoO[2]=new int[]{0,0,0};
			oO=true;
		}
		else {
			int oOOo=(OoO[0][0]+OoO[1][1]+OoO[2][2])/3+(OoO[2][0]+OoO[1][1]+OoO[0][2])/3,Oo=1;
			for(int O=0;O<3;O++) {
				oOOo+=(OoO[O][0]+OoO[O][1]+OoO[O][2])/3+(OoO[0][O]+OoO[1][O]+OoO[2][O])/3;
				for(int o=0;o<3;o++) Oo&=OoO[o][O];
			}
			if(oOOo<0) {
				oOO = "Bien joué ! Niveau suivant !";// impossible au niveau 3 :P
				OOoOO=3;
			}
			else if(Oo == 1)
				oOO = OOoOO==3?"Tu ne peux pas me battre.":"Je te laisse gagner pourtant...";
			else {
				h2(OoO, OOoOO);
				oOOo=(OoO[0][0]+OoO[1][1]+OoO[2][2])/3+(OoO[2][0]+OoO[1][1]+OoO[0][2])/3;Oo=1;
				for(int O=0;O<3;O++) {
					oOOo+=(OoO[O][0]+OoO[O][1]+OoO[O][2])/3+(OoO[0][O]+OoO[1][O]+OoO[2][O])/3;
					for(int o=0;o<3;o++) Oo&=OoO[o][O];
				}
				if(oOOo>0)
					oOO = "Oh le noob !";
				else if(Oo == 1)
					oOO = OOoOO==3?"Tu ne peux pas me battre.":"Je te laisse gagner pourtant...";
				else
					oO = true;
			}
		}
		OooOO.setText(oOO);
		OOooO.removeViewAt(2);
		OOooO.addView(g2(oOoO, OoO, oO), 2);
	}
	private static ViewGroup g2(final Context ooO, final int[][] OoO, boolean OOo){
		TableLayout ooo=new TableLayout(ooO);ooo.setLayoutParams(new LayoutParams(-2, -2));
		String oOO[] = {"\u0058"," ","O"};
		for(int O=0; O<3; O++) {
			final int OO=O;
			TableRow oO=new TableRow(ooO);
			ooo.addView(oO);
			for(int o=0;o<3;o++) {
				final int oo=o;
				Button Ooo=new Button(ooO);
				oO.addView(Ooo);Ooo.setTypeface(Typeface.MONOSPACE);
				Ooo.setText(" "+oOO[OoO[O][o]+1]+" ");
				if(OoO[O][o]==0 && OOo)
					Ooo.setOnClickListener(new OnClickListener(){
						@Override public void onClick(View v){
							OoO[OO][oo]=-1;
							f2(ooO,OoO);
					}});
				else
					Ooo.setEnabled(OoO[O][o]==0);
			}
		}
		return ooo;
	}
	private static void h2(int[][] Oo, int OOo) {
		int oO[][]={{0,0,0},{0,0,0},{0,0,0}},ooO[]={0,0,0},Ooo[]={0,0,0};
		int oOo=Oo[0][0]+Oo[1][1]+Oo[2][2],OoO=Oo[0][2]+Oo[1][1]+Oo[2][0],ooo=0,oooO=0,oooo=0;
		for(int O=0;O<3;O++)
			ooO[O]=Oo[0][O]+Oo[1][O]+Oo[2][O]+0*(Ooo[O]=Oo[O][0]+Oo[O][1]+Oo[O][2]);
		for(int O=0;O<3;O++) for(int o=0;o<3;o++) if(Oo[O][o]==0)
			if(ooO[o]==2||Ooo[O]==2||(o==O?oOo:0)==2||(o+O==2?OoO:0)==2)
				{ Oo[O][o]=1; return; }
			else if(ooO[o]==-2||Ooo[O]==-2||(o==O?oOo:0)==-2||(o+O==2?OoO:0)==-2)
				ooo+=(oooO=O+0*(oooo=o))*0+1;
		if(ooo == 1) { Oo[oooO][oooo]=1; return; }
		ooo=0;
		for(int O=0;O<3;O++) for(int o=0;o<3;o++) if(Oo[O][o]==0)
			if((oO[O][o]=o2(-ooO[o])+o2(-Ooo[O])+o2(o==O?-oOo:0)+o2(o+O==2?-OoO:0)) == 2)
				ooo+=(oooO=O+0*(oooo=o))*0+1;
		if(ooo == 1&&OOo>>1>0) { Oo[oooO][oooo]=1; return; }
		for(int OO=0;OO<3;OO++) for(int oo=0;oo<3;oo++) {
			int O=(OO+(OOo+1)/4)%3,o=(oo+(OOo+1)/4)%3;
			if(Oo[O][o]==0) {
				if(ooO[o]==1 && (Oo[(O+1)%3][o]==0?oO[(O+1)%3][o]!=2:oO[(O+2)%3][o]!=2))
					{ Oo[O][o]=1; return; }
				if(Ooo[O]==1 && (Oo[O][(o+1)%3]==0?oO[O][(o+1)%3]!=2:oO[O][(o+2)%3]!=2))
					{ Oo[O][o]=1; return; }
				if((o==O?-oOo:0)==1 && (Oo[(O+1)%3][(o+1)%3]==0?oO[(O+1)%3][(o+1)%3]!=2:oO[(O+2)%3][(o+2)%3]!=2))
					{ Oo[O][o]=1; return; }
				if((o+O==2?-OoO:0)==1 && (Oo[(O-1+3)%3][(o+1)%3]==0?oO[(O-1+3)%3][(o+1)%3]!=2:oO[(O-2+3)%3][(o+2)%3]!=2))
					{ Oo[O][o]=1; return; }
			}
		}
		for(int OO=0;OO<3;OO++) for(int oo=0;oo<3;oo++) {
			int O=(OO+(OOo+1)/4)%3,o=(oo+(OOo+1)/4)%3;
			if(Oo[O][o]==0) { Oo[O][o]=1; return; }
		}
	}
	private static int o2(int o){return o<0?0:o;}

	/** Easter Egg n°3 **/// m8p8h
	public static void ee3(RelativeLayout eaaeae) {
		/*int aeeaee = eaaeae.getWidth(), aaeaee = eaaeae.getHeight();
		ImageView eeeaae = new ImageView(eaaeae.getContext());
		ImageView eaeaae = new ImageView(eaaeae.getContext());
		Drawable aaaeea = eaaeae.getContext().getResources().getDrawable(R.drawable.d_ee3);
		int eaeeae = aaaeea.getBounds().width();
		eeeaae.setImageResource(R.drawable.f_ee3);
		eaeaae.setImageDrawable(aaaeea);
		TranslateAnimation eaeaee=new TranslateAnimation(-eaeeae*1.5f, eaeeae*2, 0, 0);
		AlphaAnimation eaaeee=new AlphaAnimation(1, 0);eaaeee.setStartOffset(2000);eaaeee.setDuration(200);
		AlphaAnimation eaeaea=new AlphaAnimation(1, 0);eaeaea.setStartOffset(4000);eaeaea.setDuration(400);
		eaeaee.setDuration(2200);eaeaee.setFillAfter(true);*/
	}
	
	/** Easter Egg n°4 **/// 9.81
	private static int Il1l = 0;
	private static Random l1=new Random();
	public static void ee4(RelativeLayout[] lI1) {
		if(l1.nextInt(4) > 0)
			return;
		for(int l=0;l<5;l++)
		for(int I=lI1[l].getChildCount();--I>-1;) {
			if(Il1l > 4) {
				TranslateAnimation lI=new TranslateAnimation(0, 0, 0, lI1[1].getHeight()<<1<<1);
				lI.setInterpolator(new AccelerateInterpolator());
				RotateAnimation Il=new RotateAnimation(0, (10<<1)*(l1.nextFloat()*(1<<1)-1), lI1[l].getChildAt(I).getWidth()>>1, 0);
				Il.setInterpolator(new LinearInterpolator());
				AnimationSet I1=new AnimationSet(false);
				I1.addAnimation(Il); I1.addAnimation(lI); I1.setDuration(4000);
				I1.setStartOffset(l1.nextInt(700));
				lI1[l].getChildAt(I).startAnimation(I1);
			} else {
				f4(3+Il1l, (Il1l*3f+7f)*(l1.nextInt(2)*2-1)*(l1.nextFloat()+1)*0.5f,
						l1.nextInt(500), lI1[l].getChildAt(I));
			}
		}
		Il1l++;
	}
	private static void f4(int lI, float l1, int l1I, final View I1l) {
		Animation Il1 = null,Ill = null;
		float l1l=l1/(lI-1);
		for(int l=0; l != lI; l++) {
			int I1=(((l&1)<<1)-1);
			final Animation Il = new RotateAnimation((l<1 ? l : (l1+l1l)*I1), -l1*I1, I1l.getWidth()/2, 0);
			Il.setInterpolator(new AccelerateDecelerateInterpolator());
			Il.setDuration(500);
			l1 -= l1l;
			if(Il1 == null)
				Il1 = Il;
			else
				Ill.setAnimationListener(new AnimationListener() {
					@Override public void onAnimationStart(Animation animation) {}
					@Override public void onAnimationRepeat(Animation animation) {}
					@Override public void onAnimationEnd(Animation animation) {
						I1l.startAnimation(Il);
					}
				});
			Ill = Il;
		}
		Il1.setStartOffset(l1I);
		I1l.setAnimation(Il1);
	}
	
	/** Easter Egg n°5 **/// ppp iv
	public static void ee5(Entry Xx, ViewGroup xX) {
		String xx=Xx.getSummary().toLowerCase(Locale.getDefault()),XX=Xx.getRoom().toLowerCase(Locale.getDefault());
		String x="e",X="o";
		if(xx.contains("t"+X+x+"ic") && Xx.getDate().get(GregorianCalendar.WEEK_OF_YEAR) == DateUtils.getCurrentWeek()) {
			ImageView xXx=(ImageView) xX.findViewById(R.id.iv_popup_ee);
			xXx.setImageResource(R.drawable.mtfbwy_64c);
			xXx.setVisibility(0);
		} else if(XX.contains(x+"xam"+x+"n") && xx.matches(".*(c"+X+
				"mpta|"+x+"c"+X+"n"+X+"mi"+x+"|g"+x+"sti"+X+"n|m"+
				"anag[?]{"+2+"}rial"+x+"s).*")) {
			ImageView xXx=(ImageView) xX.findViewById(R.id.iv_popup_ee);
			xXx.setImageResource(R.drawable.tf64_ee5);
			xXx.setVisibility(0);
		} else {
			((ImageView)xX.findViewById(R.id.iv_popup_ee)).setVisibility(8);
		}
	}
}
