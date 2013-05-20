package dao;

import control.EGHelperGUIMain;

public class EGHelperMain {

	public static void main(String[] args) {
		switch(args.length){
			case 0:{
				EGHelperGUIMain another = new EGHelperGUIMain(false);
				another.run();
			}
				break;
			case 1:
				if (args[0].equals("console")){
					EGHelperConsoleMain another = new EGHelperConsoleMain(false);
					if (another.carrier.infoMap!=null)
						another.start();
				} else if (args[0].equals("true")){
					EGHelperGUIMain another = new EGHelperGUIMain(true);
					another.run();
				} else {
					EGHelperGUIMain another = new EGHelperGUIMain(false);
					another.run();
				}
				break;
			default:{
				EGHelperConsoleMain another = new EGHelperConsoleMain(true);
				if (another.carrier.infoMap!=null)
					another.start();
			}
		}
	}

}
