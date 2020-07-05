package me.chickenstyle.stats;

public class PlayerStats {
	private int playersKilled;
	private int blocksMined;
	
	public PlayerStats(int playersKilled,int blocksMined) {
		this.playersKilled = playersKilled;
		this.blocksMined = blocksMined;
	}

	public int getPlayersKilled() {
		return playersKilled;
	}

	public void setPlayersKilled(int playersKilled) {
		this.playersKilled = playersKilled;
	}

	public int getBlocksMined() {
		return blocksMined;
	}

	public void setBlocksMined(int blocksMined) {
		this.blocksMined = blocksMined;
	}
	
	public void addPlayerKilled() {
		playersKilled = playersKilled + 1;
	}
	
	public void addBlocksMined() {
		blocksMined = blocksMined + 1;
	}
	
}
