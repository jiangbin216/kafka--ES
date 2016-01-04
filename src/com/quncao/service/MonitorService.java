package com.quncao.service;

public class MonitorService{
	public static boolean isExit = false;
	private static MonitorService monitorService = null;
	private static ConsumeService consumeService = null;

	private MonitorService(){
	}

	public static void start(){
		if (monitorService == null)
			monitorService = new MonitorService();
		if (consumeService == null ) {
			consumeService = new ConsumeService();
			consumeService.start();
		}
	}

	public static void stop(){
		if (monitorService == null)
			monitorService = new MonitorService();
		if (consumeService != null){
			consumeService.closeConsumerService();
			consumeService = null;
			isExit = true;
		}
	}

	public static boolean status(){
		if (monitorService == null)
			monitorService = new MonitorService();
		if (consumeService == null)
			return false;
		else
			return true;
	}

	public static int getRate(){
		if (monitorService == null)
			monitorService = new MonitorService();
		if (consumeService != null){
			return consumeService.getRate();
		}
		else
			return 0;
	}

/*	private static class MonitorWoker extends Thread {

		@Override
		public void run() {
			while (!isExit) {
				try {
					if (!consumeService.isAlive()) {
						consumeService = new ConsumeService();
						consumeService.start();
					}
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}*/
}


