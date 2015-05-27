package com.pijodev.insatpe;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
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
import android.widget.Toast;
import com.pijodev.insatpe.R;

public class EasterEggs {
	static void end() {
		aaaaae.removeCallbacks(eeeeea);
		eeaaee = 0;
		if(eaaeae != null)
			eaaeae.removeView(eaaeea);
		eaaeea = null;
	}
	
	/** Easter Egg n°1 **/
	static void ee1() {
		// pull ohohoho ! 
	}
	
	/** Easter Egg n°2 **/// #
	private static int Oo=0;
	public static void ee2(final Context o) {
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

	/** Easter Egg n°3 **/// <- -> [-]
	private static int eeaaee = 0;
	private static int eaeaea[][] = new int[20][10];
	private static TextView eaaeea;
	private static ViewGroup eaaeae;
	private static final String[][][] eeeaaa = {
		{{"ea","aa","ea"},{"aaa","ea"},{"a","aa","a"},{"ea","aaa"},},
		{{"a","aa","ea"},{"eaa","aa"},},
		{{"ea","aa","a"},{"aa","eaa"},},
		{{"a","a","aa"},{"eea","aaa"},{"aa","ea","ea"},{"aaa","a"},},
		{{"ea","ea","aa"},{"aaa","eea"},{"aa","a","a"},{"a","aaa"},},
		{{"a","a","a","a"},{"aaaa"},},
		{{"aa","aa"}}
	};
	private static final Random aaaaee = new Random();
	private static int[] aaaeee={0,0,20,4,'€'};
	private static final String aeaeae = "€$£₣Ұ";
	private static int eeeeee = 0;
	private static final Handler aaaaae = new Handler();
	private static final Runnable eeeeea = new Runnable() {
		@Override public void run() {
			if(f3(aaaeee[0], aaaeee[1],aaaeee[2]-1, aaaeee[3])) {//down
				aaaeee[2]--;
			}
			else if(aaaeee[2] == 20) {// end
				eeaaee = 0;
				eaaeae.removeView(eaaeea);
				eaaeea = null;
				MyToast.show(eaaeae.getContext(), "Score : "+eeeeee, Toast.LENGTH_SHORT);
				return;
			} else {// next
				h3();
				g3();
			}
			
			e3();
			aaaaae.removeCallbacks(eeeeea);
			aaaaae.postDelayed(eeeeea, Math.max(50, 300 - (int)(200*Math.log10(eeaaee+1)/2.3)));
	}};
	public static boolean ee3_locked() {
		return eeaaee >= 6;
	}
	public static void ee3_poke(boolean eaeeae) {
		eeaaee = eaeeae ? eeaaee + 1 : 0;
	}
	public static void ee3(RelativeLayout eaaeae, int aeaaea/*key*/) {
		// start
		if(eaaeea == null) {
			EasterEggs.eaaeae = eaaeae;
			i3();
			g3();
			eeeeee = 0;
			for(int a=20;--a>=0;) Arrays.fill(eaeaea[a], '.');
			aaaaae.post(eeeeea);
			return;
		}
		
		// event
		switch(aeaaea) {
			case 0:
				if(f3(aaaeee[0], aaaeee[1],aaaeee[2], aaaeee[3]-1))
					aaaeee[3]--;
				break;
			case 2:
				if(f3(aaaeee[0], aaaeee[1],aaaeee[2], aaaeee[3]+1))
					aaaeee[3]++;
				break;
			case 1:
				{int a=(aaaeee[1]+1)%eeeaaa[aaaeee[0]].length;
				if(f3(aaaeee[0], a,aaaeee[2], aaaeee[3]+1))
					aaaeee[1]=a;}
				break;
		}
		e3();
	}
	private static void i3() {
		eaaeea = new TextView(eaaeae.getContext());
		eaaeea.setTypeface(Typeface.MONOSPACE);//, Typeface.BOLD);
		eaaeea.setTextSize(16);
		eaaeea.setBackgroundColor(0xff000000);
		eaaeea.setTextColor(0xff00dd00);
		RelativeLayout.LayoutParams aeeaae = new RelativeLayout.LayoutParams(-2,-2);
		aeeaae.addRule(RelativeLayout.CENTER_IN_PARENT);
		eaaeea.setLayoutParams(aeeaae);
		eaaeae.addView(eaaeea);
		MyToast.show(eaaeae.getContext(), "Ne joue pas avec le temps, mais joue plutôt avec l'argent...", Toast.LENGTH_SHORT);
	}
	private static void h3() {
		int eae = aaaeee[2], aea = aaaeee[3], aaa = aaaeee[0], eee = aaaeee[1];
		for(int e = eae; e < Math.min(20, eae+eeeaaa[aaa][eee].length); e++)
			for(int a = aea; a < aea+eeeaaa[aaa][eee][e-eae].length(); a++)
				if(eeeaaa[aaa][eee][e-eae].charAt(a-aea)=='a')
					eaeaea[e][a] = aaaeee[4];
		int eeee = 0;
		for(int e = 0; e < 20; e++) {
			boolean aaaa = false;
			for(int a = 0; a < 10 && !aaaa; a++)
				if(eaeaea[e][a] == '.')
					aaaa = true;
			
			eaeaea[e-eeee] = eaeaea[e];
			if(!aaaa) {
				eeee++;
				eeeeee += eeee;
			}
		}
		for(;eeee > 0; eeee--) {
			eaeaea[20-eeee] = new int[10];
			Arrays.fill(eaeaea[20-eeee],  '.');
		}
	}
	private static void g3() {
		aaaeee[0] = aaaaee.nextInt(7);
		aaaeee[1] = aaaaee.nextInt(eeeaaa[aaaeee[0]].length);
		aaaeee[2] = 20;
		aaaeee[3] = 4;
		aaaeee[4] = aeaeae.codePointAt(aaaaee.nextInt(5));
	}
	private static boolean f3(int aaa, int eee, int eae, int aea) {
		for(int e = eae; e < Math.min(20, eae+eeeaaa[aaa][eee].length); e++) {
			if(e < 0) return false;
			for(int a = aea; a < aea+eeeaaa[aaa][eee][e-eae].length(); a++)
				if(a < 0 || a >= 10 || eeeaaa[aaa][eee][e-eae].charAt(a-aea)=='a' && eaeaea[e][a]!='.')
					return false;
		}
		return true;//valide
	}
	private static void e3() {
		StringBuilder aaeeae = new StringBuilder();
		for(int e = 20;--e >= 0;) {
			for(int a = 0; a < 10; a++)
				aaeeae.append(' ').appendCodePoint(eaeaea[e][a]);
			aaeeae.append(" \n");
		}
		int eae = aaaeee[2], aea = aaaeee[3], aaa = aaaeee[0], eee = aaaeee[1];
		for(int e = eae; e < Math.min(20, eae+eeeaaa[aaa][eee].length); e++)
			for(int a = aea; a < aea+eeeaaa[aaa][eee][e-eae].length(); a++)
				if(eeeaaa[aaa][eee][e-eae].charAt(a-aea) == 'a')
					aaeeae.replace(2*a+1+(2*11)*(19-e), 2*a+1+(2*11)*(19-e)+1, new String(Character.toChars(aaaeee[4])));
		
		eaaeea.setText("     T."+aeaeae.substring(0, 1)+".T.R."
				+aeaeae.substring(4, 5)+"."+aeaeae.substring(1,2)
				+"     "+"\n"+aaeeae.toString()
				+String.format(Locale.getDefault(), "   Score : %07d", eeeeee));
	}
	
	/** Easter Egg n°4 **/// 9.81	
	private static int Il1l = 0;
	private static Random l1=new Random();
	public static void ee4(RelativeLayout[] lI1) {
		if(l1.nextInt(5) > 0)
			return;
		for(int l=0;l<5;l++)
		for(int I=lI1[l].getChildCount();--I>-1;) {
			if(Il1l == 3) {
				TranslateAnimation lI=new TranslateAnimation(0, 0, 0, lI1[1].getHeight()<<1<<1);
				lI.setInterpolator(new AccelerateInterpolator());
				RotateAnimation Il=new RotateAnimation(0, (10<<1)*(l1.nextFloat()*(1<<1)-1), lI1[l].getChildAt(I).getWidth()>>1, 0);
				Il.setInterpolator(new LinearInterpolator());
				AnimationSet I1=new AnimationSet(false);
				I1.addAnimation(Il); I1.addAnimation(lI); I1.setDuration(4000);
				I1.setStartOffset(l1.nextInt(700));
				lI1[l].getChildAt(I).startAnimation(I1);
			} else if(Il1l <= 2) {
				f4(5+Il1l, (Il1l*3f+13f)*(l1.nextInt(2)*2-1)*(l1.nextFloat()+1)*0.5f,
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
		String xx=Xx.getSummary().toLowerCase(Locale.getDefault()),x="e",XX=Xx.getRoom().toLowerCase(Locale.getDefault()),X="o";
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
	
	/** Easter Egg n°6 **/// Toc ! Toc !
	@SuppressWarnings("deprecation") // setAlpha(int)
	public static void ee6(View uv) {
		uv.findViewById(R.id.iv_ee6).setOnClickListener(new OnClickListener() {
			private int vu = -1;
			private final int[] uvu = {R.drawable.ee_tt0, R.drawable.ee_tt1, R.drawable.ee_tt2, R.drawable.ee_tt3, R.drawable.ee_tt4, R.drawable.ee_tt5, R.drawable.ee_tt6};
			
			@Override
			public void onClick(View vv) {
				vu++;
				if((vu & 0xfffffffc) == 0)
					((ImageView)vv).setAlpha((1 << (vu+5)) - 1);
				if(vu < 10)
					vvuu((ImageView)vv, uvu[Math.max(vu-3,0)]);
			}
		});
		
		((ImageView)uv).setAlpha(0x18);
		vvuu((ImageView)uv, R.drawable.ee_tt0);
	}
	private static void vvuu(ImageView uv, int vv) {
		Bitmap vu = BitmapFactory.decodeResource(uv.getContext().getResources(), vv);
		int uvu = uv.getWidth(), uvv = uv.getHeight(), vuu = vu.getWidth(), vuv = vu.getHeight();
		float uuv = (vuu*uvv <= uvu*vuv ? (float)uvv/vuv : (float)uvu/vuu);
		uuv = uuv == 0 ? 1 : uuv;
		Bitmap vvu = Bitmap.createScaledBitmap(vu, (int)uuv*vuu, (int)uuv*vuv, false); 
		uv.setImageBitmap(vvu);
	}
	
}
