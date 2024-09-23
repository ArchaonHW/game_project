package game.plane;

//獎勵
public interface Award {
  public static final int LIFE = 0;//1條命
  
  public static final int DOUBLE_FIRE = 1;//雙倍火力
  //獲得獎勵類型
  int getType();
}
