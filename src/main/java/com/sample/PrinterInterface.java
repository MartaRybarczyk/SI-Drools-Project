package com.sample;

public interface PrinterInterface {
	public int getOpt() {
		int o = ShakespeareMain.sf.getOptionNum();
		System.out.print("");
		if(o!=-1) {
			ShakespeareMain.sf.resetOpt();
		}
		return o;
	}
	public void showOpt(String[] opts) {
		ShakespeareMain.sf.setOptions(opts);
	}
	public void showQ(String s) {
		ShakespeareMain.sf.setQuestion(s);
	}
	public void showFinal(String nazwa) {
		ShakespeareMain.sf.setFinal(nazwa);
	}
}
