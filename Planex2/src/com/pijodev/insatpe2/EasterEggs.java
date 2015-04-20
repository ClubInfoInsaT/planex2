package com.pijodev.insatpe2;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EasterEggs {
	static void ee1() {
		
	}
	
	/** Easter Egg n°2 **/
	private static LinearLayout OOooO;
	private static TextView OooOO;
	static void ee2(Context o) {
		OOooO = new LinearLayout(o);
		OOooO.addView(OooOO = new TextView(o));
		OooOO.setText("\u004Do\u0072\u0070i\u006f\u006E");
		OOooO.addView(new View(o));
		OOooO.setOrientation(1);
		OOooO.setGravity(1);
		new Builder(o).setView(OOooO).show();
		f2(o, null);
	}
	private static void f2(Context oOoO, int[][] OoO) {
		boolean oO=false;
		if(OoO==null) {
			OoO=new int[][]{{0,0,0,},{0,0,0,},{0,0,0,},};
			oO=true;
		}
		else {
			int oOOo=(OoO[0][0]+OoO[1][1]+OoO[2][2])/3+(OoO[2][0]+OoO[1][1]+OoO[0][2])/3,Oo=1;
			for(int O=0;O<3;O++) {
				oOOo+=(OoO[O][0]+OoO[O][1]+OoO[O][2])/3+(OoO[0][O]+OoO[1][O]+OoO[2][O])/3;
				for(int o=0;o<3;o++) Oo&=OoO[o][O];
			}
			if(oOOo<0)
				OooOO.setText("Bien joué !");// useless :P
			else if(Oo == 1)
				OooOO.setText("Oh ! Le noob !");//"\u00C9galité ! :P");
			else {
				h2(OoO);
				oOOo=(OoO[0][0]+OoO[1][1]+OoO[2][2])/3+(OoO[2][0]+OoO[1][1]+OoO[0][2])/3;Oo=1;
				for(int O=0;O<3;O++) {
					oOOo+=(OoO[O][0]+OoO[O][1]+OoO[O][2])/3+(OoO[0][O]+OoO[1][O]+OoO[2][O])/3;
					for(int o=0;o<3;o++) Oo&=OoO[o][O];
				}
				if(oOOo>0)
					OooOO.setText("Oh le noob !");
				else if(Oo == 1)
					OooOO.setText("Oh le noob !");//"\u00C9galité ! :P");
				else
					oO = true;
			}
		}

		OOooO.removeViewAt(1);
		OOooO.addView(g2(oOoO, OoO, oO), 1);
	}
	private static LinearLayout g2(final Context ooO, final int[][] OoO, boolean OOo){
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
				if(OoO[O][o]==0 && OOo) {
					Ooo.setOnClickListener(new OnClickListener(){
						@Override public void onClick(View v){
							OoO[OO][oo]=-1;
							f2(ooO,OoO);
					}});
				}
				else
					Ooo.setEnabled(OoO[O][o]==0);
			}
		}
		return ooo;
	}
	private static void h2(int[][] Oo) {
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
		for(int O=0;O<3;O++) for(int o=0;o<3;o++) if(Oo[O][o]==0)
			oO[O][o]=o2(-ooO[o])+o2(-Ooo[O])+o2(o==O?-oOo:0)+o2(o+O==2?-OoO:0);
		for(int O=0;O<3;O++) for(int o=0;o<3;o++) if(Oo[O][o]==0) {
			if(ooO[o]==1 && (Oo[(O+1)%3][o]==0?oO[(O+1)%3][o]!=2:oO[(O+2)%3][o]!=2))
				{ Oo[O][o]=1; return; }
			if(Ooo[O]==1 && (Oo[O][(o+1)%3]==0?oO[O][(o+1)%3]!=2:oO[O][(o+2)%3]!=2))
				{ Oo[O][o]=1; return; }
			if((o==O?-oOo:0)==1 && (Oo[(O+1)%3][(o+1)%3]==0?oO[(O+1)%3][(o+1)%3]!=2:oO[(O+2)%3][(o+2)%3]!=2))
				{ Oo[O][o]=1; return; }
			if((o+O==2?-OoO:0)==1 && (Oo[(O-1+3)%3][(o+1)%3]==0?oO[(O-1+3)%3][(o+1)%3]!=2:oO[(O-2+3)%3][(o+2)%3]!=2))
				{ Oo[O][o]=1; return; }
		}
		for(int O=0;O<3;O++) for(int o=0;o<3;o++) if(Oo[O][o]==0)
			{ Oo[O][o]=1; return; }
	}
	private static int o2(int o){return o<0?0:o;}
}
