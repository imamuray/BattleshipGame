import java.io.*;

public class AttackShip {
  static MapData[] map ={ new MapData(10,10,1), //2マス艦
                          new MapData(10,10,2), //3マス艦
                          new MapData(10,10,3), //4マス艦
                          new MapData(10,10,4), //5マス艦
                          new MapData(10,10,4), //合計用
                          new MapData(10,10)  //順序表示用
                         };
   static int[] max  = new int[4];
   static int[] maxX = new int[4];
   static int[] maxY = new int[4];
   
   static int[] max_arround_sum = new int[4];
   
   static int Max,MaxX,MaxY;
   static int preX,preY;
   static int dirX,dirY;
   
   static int dir_c_x = 0;
   static int dir_c_y = 0;
   static int count = 1;
   static int hit_count = 0;
   static int down_count = 0;
   
   public static void printStatus(int c,int h,int d){
      System.out.println("count: " + c);
      System.out.println("hit  : " + h);
      System.out.println("down : " + d);
   }
   
   public static void main(String[] args){
      
      sumMap();
      
      System.out.print("case6:ship2 + ship3 + ship3 + ship4 + ship5"+"\n");
      map[4].printMap();
      System.out.print("\n");
      
      int retry = 2;
      int judge = 0;
      //int hit_count = 0;
      //int down_count = 0;
      String buf;
      
     /* [1回目のdo文]
      * 撃沈が5になるまで繰り返す
      */
      do{
         try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            printStatus(count,hit_count,down_count); //状態確認
            search_map(); //map[4]におけるMaxX,MaxYを探す
            
           /* [点数表が全て0のときの分岐]
            * ログの攻撃していないところを攻撃
            */
            if(MaxX == 0 && MaxY == 0){
               int[] tmp_x = new int[100]; //[tmp_c]みたいにする xy座標を保存する
               int[] tmp_y = new int[100];
               int tmp_c = 0;
               /* map[5]で攻撃していないところを探索 */
               for(int y=1;y<=10;y++){
                  for(int x=1;x<=10;x++){
                     if(map[5].getMap(x,y) == 0){
                        tmp_x[tmp_c] = x;
                        tmp_y[tmp_c] = y;
                        tmp_c++;
                     }
                  }
               }
               int r = (int)(Math.random()*tmp_c); //ランダムに座標を指定
               MaxX = tmp_x[r];
               MaxY = tmp_y[r];
            }
            
            attack_ship(MaxX,MaxY); //(MaxX,MaxY)を攻撃
            count++;
            
            /* 判定入力 */
            do{
               System.out.print("Judge: 1...mis/2...hit/3...down" +"\n");
               buf = br.readLine();
               judge = Integer.parseInt(buf);
            }while(judge != 1 && judge != 2 && judge != 3); //判定入力終了
           /* [1...misのとき]
            * もう一度全体を探索する
            */
            if(judge == 1){
               continue;
            }
           /* [3...downのとき]
            * 命中数と撃沈数を数えてもう一度全体を探索する
            */
            else if(judge == 3){
               hit_count++;
               down_count++;
               continue;
            }
           /* [2...misのとき]
            * 命中数を数えて分岐1へ
            */
            else if(judge == 2){ //分岐1
               hit_count++;
               /* [2回目のdo文]
                * 撃沈が出るまで繰り返す
                */
               do{
                  System.out.println("分岐1");
                  printStatus(count,hit_count,down_count); //状態確認
                  
                  preX = MaxX; //分岐1に入る前に攻撃した座標をpreX,preYに保存
                  preY = MaxY;
                  
                  int[] preXlr = {preX  ,preX+1,preX  ,preX-1}; //left,right
                  int[] preYud = {preY+1,preY  ,preY-1,preY  }; //up,down
                  int[] preXtmp = new int[4];
                  int[] preYtmp = new int[4];
                  int preTmp = 0;
                  /* map[5]で(preX,preY)の周囲探索 */
                  
                  for(int i=0;i<4;i++){
                     if( preXlr[i] >= 1 && preXlr[i] <= 10 &&
                         preYud[i] >= 1 && preYud[i] <= 10 &&
                         map[5].getMap(preXlr[i],preYud[i]) == 0){
                        preXtmp[preTmp] = preXlr[i];
                        preYtmp[preTmp] = preYud[i];
                        preTmp++;
                     }
                  }
                  
                  
                  /*　//プロトタイプ
                  for(int y=0;y<4;y++){
                     for(int x=0;x<4;x++){
                        if( preXlr[x] >= 1 && preXlr[x] <= 10 &&
                            preYud[y] >= 1 && preYud[y] <= 10 &&
                            map[5].getMap(preXlr[x],preYud[y]) == 0){
                           preXtmp[preTmp] = preXlr[x];
                           preYtmp[preTmp] = preYud[y];
                           preTmp++;
                        }
                     }
                  }
                  */
                  
                 /* map[5]で0のところの点数を調べる
                  * 点数が高ければ優先してその座標を攻撃
                  * 点数が全て0のときはランダム（する必要なさそう）
                  */
                  int[] compareUDLR = new int[4];
                  arround_sum(compareUDLR, preXtmp, preYtmp);
                  MaxX = preXtmp[0];
                  MaxY = preYtmp[0];
                  for(int i=0; i<preTmp; i++){
                     if( compareUDLR[0] <= compareUDLR[i] ){
                        compareUDLR[0] = compareUDLR[i];
                        MaxX = preXtmp[i];
                        MaxY = preYtmp[i];
                     }
                  }
                  
                  /* //プロトタイプ
                  int compareUDLR = map[4].getMap(preXtmp[0],preYtmp[0]);
                  MaxX = preXtmp[0];
                  MaxY = preYtmp[0];
                  for(int i=0; i<preTmp; i++){
                     if( compareUDLR <= map[4].getMap(preXtmp[i],preYtmp[i]) ){
                        compareUDLR = map[4].getMap(preXtmp[i],preYtmp[i]);
                        MaxX = preXtmp[i];
                        MaxY = preYtmp[i];
                     }
                  }
                  */
                  attack_ship(MaxX,MaxY); //(MaxX,MaxY)を攻撃
                  
                  dirX = MaxX; //攻撃した座標をdirX,dirYに保存
                  dirY = MaxY;
                  
                  count++;
                  
                  /* 判定入力 */
                  do{
                     System.out.print("Judge: 1...mis/2...hit/3...down" +"\n");
                     buf = br.readLine();
                     judge = Integer.parseInt(buf);
                  }while(judge != 1 && judge != 2 && judge != 3); //判定入力終了
                 /* [1...misのとき]
                  * もう一度分岐1を繰り返す
                  */
                  if(judge == 1){
                     MaxX = preX;
                     MaxY = preY;
                     continue;
                  }
                 /* [3...downのとき]
                  * 命中数と撃沈数を数えてもう一度全体を探索する
                  */
                  else if(judge == 3){
                     hit_count++;
                     down_count++;
                  }
                 /* [2...misのとき]
                  * 命中数を数えて分岐2へ
                  */
                  else if(judge == 2){ //分岐2
                     hit_count++;
                    /* [3回目のdo文]
                     * 撃沈が出るまで繰り返す
                     */
                     do{
                        System.out.println("分岐2");
                        printStatus(count,hit_count,down_count); //状態確認
                        
                        judge_dir(); //伸ばした方向の確認
                        if(dirX >= 1 && dirX <= 10 &&
                           dirY >= 1 && dirY <= 10 &&
                           map[5].getMap(dirX,dirY) == 0){
                           attack_ship(dirX,dirY);
                           count++;
                        }else{System.out.println("Please press 1..mis");}
                        
                        /* 判定入力 */
                        do{
                           System.out.print("Judge: 1...mis/2...hit/3...down" +"\n");
                           buf = br.readLine();
                           judge = Integer.parseInt(buf);
                        }while(judge != 1 && judge != 2 && judge != 3); //判定入力終了
                       /* [2...misのとき]
                        * 命中を数えて分岐2を繰り返す
                        */
                        if(judge == 2){
                           hit_count++;
                           continue;
                        }
                       /* [3...downのとき]
                        * 命中数,撃沈数,dir_c_x,dir_c_yを数えてもう一度全体を探索する
                        */
                        else if(judge == 3){
                           hit_count++;
                           down_count++;
                           dir_c_x = 0;
                           dir_c_y = 0;
                        }
                       /* [1...misのとき]
                        * 逆方向へ攻撃する分岐3へ
                        */
                        else if(judge == 1){ //分岐3
                           System.out.println("分岐3");
                           printStatus(count,hit_count,down_count); //状態確認
                           System.out.println("dir_c_x: " + dir_c_x);
                           System.out.println("dir_c_y: " + dir_c_y);
                           
                           if(dir_c_x > 0 && preX-1 > 0 && map[5].getMap(preX-1,preY) == 0){
                              attack_ship(preX-1,preY);
                              dirX = preX-1;
                              dir_c_x = 0;
                              dir_c_y = 0;
                              count++;
                           }else
                           if(dir_c_x < 0 && preX+1 < 11 && map[5].getMap(preX+1,preY) == 0){
                              attack_ship(preX+1,preY);
                              dirX = preX+1;
                              dir_c_x = 0;
                              dir_c_y = 0;
                              count++;
                           }else
                           if(dir_c_y > 0 && preY-1 > 0 && map[5].getMap(preX,preY-1) == 0){
                              attack_ship(preX,preY-1);
                              dirY = preY-1;
                              dir_c_x = 0;
                              dir_c_y = 0;
                              count++;
                           }else
                           if(dir_c_y < 0 && preY+1 < 11 && map[5].getMap(preX,preY+1) == 0){
                              attack_ship(preX,preY+1);
                              dirY = preY+1;
                              dir_c_x = 0;
                              dir_c_y = 0;
                              count++;
                           }else {
                              System.out.println("Please press 1..mis");
                              System.out.println("preX: " + preX);
                              System.out.println("preY: " + preY);
                              System.out.println("dirX: " + dirX);
                              System.out.println("dirY: " + dirY);
                           }
                           /* 判定入力 */
                           do{
                              System.out.print("Judge: 1...mis/2...hit/3...down" +"\n");
                              buf = br.readLine();
                              judge = Integer.parseInt(buf);
                           }while(judge != 1 && judge != 2 && judge != 3); //判定入力終了
                          /* [1...misのとき]
                           * 分岐1へ
                           */
                           
                          /* [2...misのとき]
                           * 分岐2へ
                           */
                           if(judge == 2){
                              continue;
                           }
                          /* [3...downのとき]
                           * 命中数,撃沈数,dir_c_x,dir_c_yを数えてもう一度全体を探索する
                           */
                           else if(judge == 3){
                              hit_count++;
                              down_count++;
                              dir_c_x = 0;
                              dir_c_y = 0;
                           }
                        } //分岐3終了
                        
                     }while(judge != 3 && judge != 1); //[3回目のdo文]終了
                  } //分岐2終了
                  judge = 3; //分岐2がdownで終了したときのための処理
               }while(judge != 3); //[2回目のdo文]終了
            } //分岐1終了
            
         }catch(Exception e){
            System.err.print("Error:" + e);
         }
      }while(down_count < 5); //[1回目のdo文]終了
   }
   
   public static void attack_ship(int x, int y){   
      String[] Xmoji = {"null","A","B","C","D","E","F","G","H","I","J","K","L"};
      for(int i=0; i<4; i++){
         map[i].setMap(x,y,0);
         map[i].changeMap();
      }
      System.out.print("\n" + "Change map data: point(" + Xmoji[x] + "," + y + ")" +"\n");    
      sumMap();    
      map[4].printMap();
         
      System.out.println();
      map[5].setMap(x,y,count);
      map[5].printMap();
      System.out.print("\n");
   }
   
   
   public static void judge_dir(){
      if(dirX-preX > 0){
         dirX += 1;
         dir_c_x++;
      }
      if(dirX-preX < 0){
         dirX -= 1;
         dir_c_x--;
      }
      if(dirY-preY > 0){
         dirY += 1;
         dir_c_y++;
      }
      if(dirY-preY < 0){
         dirY -= 1;
         dir_c_y--;
      }
   }
   
   public static void search_arround(int x,int y){
      int[] arround = new int[4];
      int[] arrX = new int[4];
      int[] arrY = new int[4]; 
      
      arrX[0] = x  ; arrY[0] = y+1;
      arrX[1] = x+1; arrY[1] = y  ;
      arrX[2] = x  ; arrY[2] = y-1;
      arrX[3] = x-1; arrY[3] = y  ;
      
      arround_sum(arround,arrX,arrY);
      chooseMax(arround,arrX,arrY);
   }
   
   public static void search_map(){
      
      for(int i=0; i<max.length; i++){
         max[i]  = 0;
         maxX[i] = 0;
         maxY[i] = 0;
      }
      
      for(int y=1; y<= map[4].getHeight()/2; y++){
         for(int x=1; x<= map[4].getWidth()/2; x++){
            if(map[4].getMap(x,y) > max[0]){
               max[0] = map[4].getMap(x,y);
               maxX[0] = x;
               maxY[0] = y;
            }
         }
         for(int x= map[4].getWidth(); x > map[4].getWidth()/2; x--){
            if(map[4].getMap(x,y) > max[1]){
               max[1] = map[4].getMap(x,y);
               maxX[1] = x;
               maxY[1] = y;
            }
         }
      }
      
      for(int y= map[4].getHeight(); y > map[4].getHeight()/2; y--){
         for(int x=1; x<= map[4].getWidth()/2; x++){
            if(map[4].getMap(x,y) > max[2]){
               max[2] = map[4].getMap(x,y);
               maxX[2] = x;
               maxY[2] = y;
            }
         }
         for(int x= map[4].getWidth(); x > map[4].getWidth()/2; x--){
            if(map[4].getMap(x,y) > max[3]){
               max[3] = map[4].getMap(x,y);
               maxX[3] = x;
               maxY[3] = y;
            }
         }
      }
      
      System.out.println("max[0] = " + max[0] + ", ( " + maxX[0] + ", " + maxY[0] +" )");
      System.out.println("max[1] = " + max[1] + ", ( " + maxX[1] + ", " + maxY[1] +" )");
      System.out.println("max[2] = " + max[2] + ", ( " + maxX[2] + ", " + maxY[2] +" )");
      System.out.println("max[3] = " + max[3] + ", ( " + maxX[3] + ", " + maxY[3] +" )");
      
      /* //re5.txt
      if(count < 21){
         chooseMax(max, maxX, maxY);
      } else {chooseMax(max_arround_sum, maxX, maxY);}
      */
      
      /* //re4.txt
      if(count < 21){
         chooseMax(max_arround_sum, maxX, maxY);
      } else {chooseMax(max, maxX, maxY);}
      */
      
      /* 下1行を使うと最適探索 */
      //chooseMax(max, maxX, maxY);
      
      /* 下2行を使うと周囲4マスを考慮した探索になる */
      
      arround_sum(max_arround_sum, maxX, maxY);
      chooseMax(max_arround_sum, maxX, maxY); //re.txt
      
   }
   
   public static void arround_sum(int[] m, int[] x, int[] y){
      for(int i=0; i< m.length; i++){
         m[i] = 0;
      }
      for(int i=0; i<m.length; i++){
         if( x[i] > 0 && y[i] > 0 && x[i] < 11 && y[i] < 11){
            m[i] = map[4].getMap(x[i]-1,y[i]  ) +
                   map[4].getMap(x[i]+1,y[i]  ) +
                   map[4].getMap(x[i]  ,y[i]-1) +
                   map[4].getMap(x[i]  ,y[i]+1) +
                   map[4].getMap(x[i]  ,y[i]  ) ;
            System.out.println("arround_sum["+i+"] = " + m[i] );
         }
      }
   }
   
   public static void chooseMax(int[] m, int[] x, int[] y){
      Max  =0;
      MaxX =0;
      MaxY =0;
      
      for(int i=0; i< m.length; i++){
         if(m[i] > Max){
            Max = m[i];
            MaxX = x[i];
            MaxY = y[i];
         }
      }
      System.out.println("  max  : " + Max + ", ( " + MaxX + ", " + MaxY +" )");
   }
   
   public static void sumMap(){
      for(int y=1; y<=map[3].getHeight(); y++){   
         for(int x=1; x<=map[3].getWidth(); x++){
         map[4].setMap( x, y,
                        ( map[0].getMap(x,y) + map[1].getMap(x,y) +
                          map[1].getMap(x,y) + map[2].getMap(x,y) +
                          map[3].getMap(x,y)) );
         }
      }
   }
}
