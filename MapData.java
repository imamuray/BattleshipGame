
public class MapData {
   
   private static int count = 1;
   private static String[] az = {"A","B","C","D","E","F","G","H","I","J","K","L"};
   private int id;
   private int[][] map;
   private int width;
   private int height;
   private int out_width;
   private int out_height;
   private int ship;
   
   MapData(int x, int y , int s){
      
      width = x;
      height = y;
      out_width = x+2;
      out_height = y+2;
      ship = s;
      map = new int[x+2][y+2];
      
      fillMap();
      
      this.id = count;
      count++;
   }
   
   MapData(int x, int y){
      
      width = x;
      height = y;
      out_width = x+2;
      out_height = y+2;

      map = new int[x+2][y+2];
            
      this.id = count;
      count++;
   }
   
   public int getHeight(){
      return height;
   }
   
   public int getWidth(){
      return width;
   }
   
   public int getShip(){
      return ship;
   }
   
   public int getMap(int x, int y){
      return map[x][y];
   }
   
   public void setMap(int x, int y, int map){
      this.map[x][y] = map; 
   }
   
   
   public void fillMap(){
      
      //点数の計算を行います
      int i,j;
      
      for(int y=1; y<=height; y++){
         for(int x=1; x<=width; x++){
            i = x - ship;
            j = x;
            while(i <= x){
               if(i >= 1 && j <= width){
                  this.map[x][y] ++;
               }
               i++;
               j++;
            }
         
            i = y - ship;
            j = y;
            while(i <= y){
               if(i >= 1 && j <= height){
                  this.map[x][y] ++;
               }
               i++;
               j++;
            }
         }
      }
   }
   
   public void changeMap(){
      int c = 0;
      int d = 0;
      int sum;
      int[][] index = new int[width+2][height+2];
      
      //計算する
      for(int y=1; y<=height; y++){
         for(int x=1; x<=width; x++){
            for(int i=x; this.map[i][y] != 0 && c < this.ship+1 ; i--){ c++; }
            for(int i=x; this.map[i][y] != 0 && d < this.ship+1 ; i++){ d++; }
            sum = c+d - (ship+1);
            if(sum < 0){sum = 0;}
            index[x][y] += sum; 
            c = 0;
            d = 0;
            sum=0;
         }
      }
      
      for(int x=1; x<=width; x++){
         for(int y=1; y<=height; y++){
            for(int i=y; this.map[x][i] != 0 && c < this.ship+1 ; i--){ c++; }
            for(int i=y; this.map[x][i] != 0 && d < this.ship+1 ; i++){ d++; }
            sum = c+d - (ship+1);
            if(sum < 0){sum = 0;}
            index[x][y] += sum; 
            c = 0;
            d = 0;
            sum=0;
         }
      }
      
      //代入する
      for(int y=1; y<=height; y++){
         for(int x=1; x<=width; x++){
            
            this.map[x][y] = index[x][y];
         }
      }
   }
   
   
   public void printMap(){
      //実際使う用
      // /*
      System.out.print("     ");
      for(int i=0; i<width; i++){
         System.out.print("  " + az[i]);
      }
      System.out.print("\n");
      System.out.print("     ");
      for(int i=0; i<width; i++){
         System.out.print("---");
      }
      System.out.print("\n");
      
      for(int y=1; y<=height; y++){
         System.out.printf(String.format("%3d",y));
         System.out.print(" |");
         for(int x=1; x<=width; x++){
            System.out.printf(String.format("%3d",getMap(x,y)));
         }
         System.out.print("\n");
      }
      // */
      
      //テスト用
      //周りが-1で囲まれていることを確認できます
      /*
      System.out.print("     ");
      for(int i=0; i<out_width; i++){
         System.out.print("  " + az[i]);
      }
      System.out.print("\n");
      System.out.print("     ");
      for(int i=0; i<out_width; i++){
         System.out.print("---");
      }
      System.out.print("\n");
      
      for(int y=0; y<out_height; y++){
         System.out.printf(String.format("%3d",y+1));
         System.out.print(" |");
         for(int x=0; x<out_width; x++){
            System.out.printf(String.format("%3d",getMap(x,y)));
         }
         System.out.print("\n");
      }
      */
   }
}

